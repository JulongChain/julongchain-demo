package demonstration.operations;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import demonstration.model.BlockInfo;
import io.grpc.stub.StreamObserver;
import org.bcia.julongchain.common.exception.JavaChainException;
import org.bcia.julongchain.common.exception.NodeException;
import org.bcia.julongchain.common.log.JavaChainLog;
import org.bcia.julongchain.common.log.JavaChainLogFactory;
import org.bcia.julongchain.common.protos.EnvelopeVO;
import org.bcia.julongchain.common.protos.IProtoVO;
import org.bcia.julongchain.common.util.proto.EnvelopeHelper;
import org.bcia.julongchain.common.util.proto.ProposalUtils;

import org.bcia.julongchain.csp.factory.CspManager;
import org.bcia.julongchain.msp.ISigningIdentity;
import org.bcia.julongchain.msp.mgmt.GlobalMspManagement;
import org.bcia.julongchain.node.Node;
import org.bcia.julongchain.node.common.client.BroadcastClient;
import org.bcia.julongchain.node.common.client.EndorserClient;
import org.bcia.julongchain.node.common.client.IBroadcastClient;
import org.bcia.julongchain.node.common.helper.SpecHelper;
import org.bcia.julongchain.protos.common.Common;
import org.bcia.julongchain.protos.common.Ledger;
import org.bcia.julongchain.protos.consenter.Ab;
import org.bcia.julongchain.protos.ledger.rwset.Rwset;
import org.bcia.julongchain.protos.ledger.rwset.kvrwset.KvRwset;
import org.bcia.julongchain.protos.node.ProposalPackage;
import org.bcia.julongchain.protos.node.ProposalResponsePackage;
import org.bcia.julongchain.protos.node.SmartContractPackage;
import org.bcia.julongchain.protos.node.TransactionPackage;

import javax.xml.bind.DatatypeConverter;

/**
 * @author lishaojie
 * @Date: 2018/7/2
 * @company Dingxuan
 */
public class NodeSmartContract {

    private static JavaChainLog log = JavaChainLogFactory.getLog(NodeSmartContract.class);

    private Node node;

    public NodeSmartContract() {
    }

    public NodeSmartContract(Node node) {
        this.node = node;
    }


    public String invoke(String ip, int port, String endorserhost, int endorserPort, String groupId, String scName, String scLanguage, SmartContractPackage
            .SmartContractInput input) throws NodeException {
        SmartContractPackage.SmartContractInvocationSpec sciSpec = SpecHelper.buildInvocationSpec(scName, input);

        ISigningIdentity identity = GlobalMspManagement.getLocalMsp().getDefaultSigningIdentity();

        byte[] creator = identity.getIdentity().serialize();

        byte[] nonce = new byte[0];
        try {
            nonce = CspManager.getDefaultCsp().rng(24, null);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
        }

        String txId = null;
        try {
            txId = ProposalUtils.computeProposalTxID(creator, nonce);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
            throw new NodeException("Generate txId fail");
        }

        //build proposal
        ProposalPackage.Proposal proposal = ProposalUtils.buildSmartContractProposal(Common.HeaderType.ENDORSER_TRANSACTION,
                groupId, txId, sciSpec, nonce, creator, null);
        //build signedProposal
        ProposalPackage.SignedProposal signedProposal = ProposalUtils.buildSignedProposal(proposal, identity);

        //背书
//        EndorserClient client = new EndorserClient(ENDORSER_HOST, ENDORSER__PORT);
        EndorserClient client = new EndorserClient(endorserhost, endorserPort);
        ProposalResponsePackage.ProposalResponse proposalResponse = client.sendProcessProposal(signedProposal);

        try {
            Common.Envelope signedTxEnvelope = EnvelopeHelper.createSignedTxEnvelope(proposal, identity, proposalResponse);

            EnvelopeVO envelopeVO = new EnvelopeVO();
            envelopeVO.parseFrom(signedTxEnvelope);

            final IBroadcastClient broadcastClient = new BroadcastClient(ip, port);
            broadcastClient.send(signedTxEnvelope, new StreamObserver<Ab.BroadcastResponse>() {
                @Override
                public void onNext(Ab.BroadcastResponse value) {
                    log.info("Broadcast onNext");
                    broadcastClient.close();

                    //收到响应消息，判断是否是200消息
                    if (Common.Status.SUCCESS.equals(value.getStatus())) {
                        log.info("invoke success");
                    }
                }

                @Override
                public void onError(Throwable t) {
                    log.error("onError");
                    log.error(t.getMessage(), t);
                    broadcastClient.close();
                }

                @Override
                public void onCompleted() {
                    log.info("Broadcast completed");
                    broadcastClient.close();
                }
            });

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NodeException(e);
        }

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            log.error(e.getMessage(), e);
//        }
        return "success";
    }

    public long height(String endorserhost, int endorserPort, String groupId, String smartContractName, SmartContractPackage.SmartContractInput input) throws NodeException {
        SmartContractPackage.SmartContractInvocationSpec spec = SpecHelper.buildInvocationSpec(smartContractName, input);

        ISigningIdentity identity = GlobalMspManagement.getLocalMsp().getDefaultSigningIdentity();

        byte[] creator = identity.getIdentity().serialize();

        byte[] nonce = new byte[0];
        try {
            nonce = CspManager.getDefaultCsp().rng(24, null);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
        }

        String txId = null;
        try {
            txId = ProposalUtils.computeProposalTxID(creator, nonce);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
            throw new NodeException("Generate txId fail");
        }

        ProposalPackage.Proposal proposal = ProposalUtils.buildSmartContractProposal(Common.HeaderType
                .ENDORSER_TRANSACTION, groupId, txId, spec, nonce, creator, null);
        ProposalPackage.SignedProposal signedProposal = ProposalUtils.buildSignedProposal(proposal, identity);

        EndorserClient client = new EndorserClient(endorserhost, endorserPort);
        ProposalResponsePackage.ProposalResponse proposalResponse = client.sendProcessProposal(signedProposal);
        long height=0;
        ByteString payload = proposalResponse.getResponse().getPayload();
        try {
            Ledger.BlockchainInfo xxx= Ledger.BlockchainInfo.parseFrom(payload);
            height=xxx.getHeight();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

return height;
    }

    public String block(String endorserhost, int endorserPort, String groupId, String smartContractName, SmartContractPackage.SmartContractInput input) throws NodeException {
        SmartContractPackage.SmartContractInvocationSpec spec = SpecHelper.buildInvocationSpec(smartContractName, input);

        ISigningIdentity identity = GlobalMspManagement.getLocalMsp().getDefaultSigningIdentity();

        byte[] creator = identity.getIdentity().serialize();

        byte[] nonce = new byte[0];
        try {
            nonce = CspManager.getDefaultCsp().rng(24, null);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
        }

        String txId = null;
        try {
            txId = ProposalUtils.computeProposalTxID(creator, nonce);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
            throw new NodeException("Generate txId fail");
        }

        ProposalPackage.Proposal proposal = ProposalUtils.buildSmartContractProposal(Common.HeaderType
                .ENDORSER_TRANSACTION, groupId, txId, spec, nonce, creator, null);
        ProposalPackage.SignedProposal signedProposal = ProposalUtils.buildSignedProposal(proposal, identity);

        EndorserClient client = new EndorserClient(endorserhost, endorserPort);
        ProposalResponsePackage.ProposalResponse proposalResponse = client.sendProcessProposal(signedProposal);

        ByteString payload = proposalResponse.getResponse().getPayload();

        log.info("Query Result: " + payload);

        Common.Block block = null;
        EnvelopeVO envelopeVO = new EnvelopeVO();
        try {
            block = Common.Block.parseFrom(payload);
            envelopeVO.parseFrom(Common.Envelope.parseFrom(block.getData().getData(0)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        BlockInfo BI=new BlockInfo();
        if (block != null) {
            byte[] dataHash = block.getHeader().getDataHash().toByteArray();//当前hash
            byte[] previousHash = block.getHeader().getPreviousHash().toByteArray();//前hash
            long numid = block.getHeader().getNumber();//块号
            //String timeStamp = envelopeVO.getPayloadVO().getGroupHeaderVO().getTimeStamp().toString();
            String txid = envelopeVO.getPayloadVO().getGroupHeaderVO().getTxId();//交易id
            IProtoVO rw = envelopeVO.getPayloadVO().getDataVO();
            String aa = rw.toString();

            String dh = DatatypeConverter.printHexBinary(dataHash);
            String ph = DatatypeConverter.printHexBinary(previousHash);


        BI.setDataHash(dh);
        BI.setPreviousHash(ph);
        BI.setNumid(numid);
        BI.setTxid(txid);
    }
//        Common.Envelope envelope = null;
//        try {
//            envelope = Common.Envelope.parseFrom(block.getData().getData(0));
//
//            Common.Payload payload1 = Common.Payload.parseFrom(envelope.getPayload());
//            TransactionPackage.Transaction transaction = TransactionPackage.Transaction.parseFrom(payload1.getData());
//            TransactionPackage.TransactionAction transactionAction = transaction.getActions(0);
//            TransactionPackage.SmartContractActionPayload scaPayload = TransactionPackage.SmartContractActionPayload.parseFrom(transactionAction.getPayload());
//            TransactionPackage.SmartContractEndorsedAction sceaPayload = scaPayload.getAction();
//            ProposalResponsePackage.ProposalResponsePayload prPayload = ProposalResponsePackage.ProposalResponsePayload.parseFrom(sceaPayload.getProposalResponsePayload());
//            ProposalPackage.SmartContractAction resPayload = ProposalPackage.SmartContractAction.parseFrom(prPayload.getExtension());
//            Rwset.TxReadWriteSet txReadWriteSet = Rwset.TxReadWriteSet.parseFrom(resPayload.getResults());
//            KvRwset.KVRWSet kvrwSet = KvRwset.KVRWSet.parseFrom(txReadWriteSet.getNsRwset(0).getRwset());
//            System.out.print(1);
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }

        String jsonString = JSONObject.toJSONString(BI);
        return jsonString;
    }

    public String query(String endorserhost, int endorserPort, String groupId, String smartContractName, SmartContractPackage.SmartContractInput input) throws NodeException {
        SmartContractPackage.SmartContractInvocationSpec spec = SpecHelper.buildInvocationSpec(smartContractName, input);

        ISigningIdentity identity = GlobalMspManagement.getLocalMsp().getDefaultSigningIdentity();

        byte[] creator = identity.getIdentity().serialize();

        byte[] nonce = new byte[0];
        try {
            nonce = CspManager.getDefaultCsp().rng(24, null);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
        }

        String txId = null;
        try {
            txId = ProposalUtils.computeProposalTxID(creator, nonce);
        } catch (JavaChainException e) {
            log.error(e.getMessage(), e);
            throw new NodeException("Generate txId fail");
        }

        ProposalPackage.Proposal proposal = ProposalUtils.buildSmartContractProposal(Common.HeaderType
                .ENDORSER_TRANSACTION, groupId, txId, spec, nonce, creator, null);
        ProposalPackage.SignedProposal signedProposal = ProposalUtils.buildSignedProposal(proposal, identity);

        EndorserClient client = new EndorserClient(endorserhost, endorserPort);
        ProposalResponsePackage.ProposalResponse proposalResponse = client.sendProcessProposal(signedProposal);

        ByteString payload = proposalResponse.getResponse().getPayload();
        String message = proposalResponse.getResponse().getMessage();
        System.out.println("============message:" + message);


        log.info("Query Result: " + message);
//                .getPayload().toStringUtf8());
        return message;
    }
}
