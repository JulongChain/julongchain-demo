package auction.operations;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import org.bcia.julongchain.common.exception.JavaChainException;
import org.bcia.julongchain.common.exception.NodeException;
import org.bcia.julongchain.common.exception.ValidateException;
import org.bcia.julongchain.common.log.JavaChainLog;
import org.bcia.julongchain.common.log.JavaChainLogFactory;
import org.bcia.julongchain.common.protos.EnvelopeVO;
import org.bcia.julongchain.common.util.CommConstant;
import org.bcia.julongchain.common.util.proto.EnvelopeHelper;
import org.bcia.julongchain.common.util.proto.ProposalUtils;

import org.bcia.julongchain.core.ssc.lssc.LSSC;
import org.bcia.julongchain.csp.factory.CspManager;
import org.bcia.julongchain.msp.ISigningIdentity;
import org.bcia.julongchain.msp.mgmt.GlobalMspManagement;
import org.bcia.julongchain.node.Node;
import org.bcia.julongchain.node.common.client.BroadcastClient;
import org.bcia.julongchain.node.common.client.EndorserClient;
import org.bcia.julongchain.node.common.client.IBroadcastClient;
import org.bcia.julongchain.node.common.helper.SpecHelper;
import org.bcia.julongchain.protos.common.Common;
import org.bcia.julongchain.protos.consenter.Ab;
import org.bcia.julongchain.protos.node.ProposalPackage;
import org.bcia.julongchain.protos.node.ProposalResponsePackage;
import org.bcia.julongchain.protos.node.SmartContractPackage;
import org.bcia.julongchain.protos.node.SmartContractShim;

/**
 * @author lishaojie
 * @Date: 2018/7/2
 * @company Dingxuan
 */
public class NodeSmartContract {


    private static final String ENDORSER_HOST = "192.168.1.155";
    private static final int ENDORSER__PORT = 10051;
    private static JavaChainLog log = JavaChainLogFactory.getLog(NodeSmartContract.class);

    private Node node;

    public NodeSmartContract() {
    }

    public NodeSmartContract(Node node) {
        this.node = node;
    }

    public void install(String scName, String scVersion, String scPath, String scLanguage, SmartContractPackage
            .SmartContractInput input) throws NodeException {
        SmartContractPackage.SmartContractDeploymentSpec deploymentSpec = SpecHelper.buildDeploymentSpec(scName, scVersion,
                scPath, input);

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

        byte[] inputBytes = (input != null ? input.toByteArray() : new byte[0]);
        SmartContractPackage.SmartContractInvocationSpec lsscSpec = SpecHelper.buildInvocationSpec(CommConstant.LSSC,
                LSSC.INSTALL.getBytes(), deploymentSpec.toByteArray());
        //生成proposal  Type=ENDORSER_TRANSACTION
        ProposalPackage.Proposal proposal = ProposalUtils.buildSmartContractProposal(Common.HeaderType
                .ENDORSER_TRANSACTION, "", txId, lsscSpec, nonce, creator, null);
        ProposalPackage.SignedProposal signedProposal = ProposalUtils.buildSignedProposal(proposal, identity);

        //获取背书节点返回信息
        EndorserClient client = new EndorserClient(ENDORSER_HOST, ENDORSER__PORT);
        ProposalResponsePackage.ProposalResponse proposalResponse = client.sendProcessProposal(signedProposal);
        log.info("Install Result: " + proposalResponse.getResponse().getStatus());
    }

    public void instantiate(String ip, int port, String groupId, String scName, String scVersion, SmartContractPackage
            .SmartContractInput input) throws NodeException {
        SmartContractPackage.SmartContractDeploymentSpec deploymentSpec = SpecHelper.buildDeploymentSpec(scName, scVersion,
                null, input);

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

        SmartContractPackage.SmartContractInvocationSpec lsscSpec = SpecHelper.buildInvocationSpec(CommConstant.LSSC,
                CommConstant.DEPLOY.getBytes(), groupId.getBytes(), deploymentSpec.toByteArray());
        //生成proposal  Type=ENDORSER_TRANSACTION
        ProposalPackage.Proposal proposal = ProposalUtils.buildSmartContractProposal(Common.HeaderType
                .ENDORSER_TRANSACTION, groupId, txId, lsscSpec, nonce, creator, null);
        ProposalPackage.SignedProposal signedProposal = ProposalUtils.buildSignedProposal(proposal, identity);

        //获取背书节点返回信息
        EndorserClient client = new EndorserClient(ENDORSER_HOST, ENDORSER__PORT);
        ProposalResponsePackage.ProposalResponse proposalResponse = client.sendProcessProposal(signedProposal);

        try {
            Common.Envelope signedTxEnvelope = EnvelopeHelper.createSignedTxEnvelope(proposal, identity, proposalResponse);

            EnvelopeVO envelopeVO = new EnvelopeVO();
            try {
                envelopeVO.parseFrom(signedTxEnvelope);
            } catch (InvalidProtocolBufferException e) {
                log.error(e.getMessage(), e);
            }

            final IBroadcastClient broadcastClient = new BroadcastClient(ip, port);
            broadcastClient.send(signedTxEnvelope, new StreamObserver<Ab.BroadcastResponse>() {
                @Override
                public void onNext(Ab.BroadcastResponse value) {
                    log.info("Broadcast onNext");
                    broadcastClient.close();

                    //收到响应消息，判断是否是200消息
                    if (Common.Status.SUCCESS.equals(value.getStatus())) {
                        log.info("instantiate success");
                    }
                }

                @Override
                public void onError(Throwable t) {
                    log.error(t.getMessage(), t);
                    broadcastClient.close();
                }

                @Override
                public void onCompleted() {
                    log.info("Broadcast completed");
                    broadcastClient.close();
                }
            });

        } catch (ValidateException e) {
            log.error(e.getMessage(), e);
            throw new NodeException(e);
        }

        try {
            Thread.sleep(900000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

    }

    public String invoke(String ip, int port, String groupId, String scName, String scLanguage, SmartContractPackage
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
        EndorserClient client = new EndorserClient(ENDORSER_HOST, ENDORSER__PORT);
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

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return "success";
    }

    public String query(String groupId, String smartContractName, SmartContractPackage.SmartContractInput input) throws NodeException {
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

        EndorserClient client = new EndorserClient(ENDORSER_HOST, ENDORSER__PORT);
        ProposalResponsePackage.ProposalResponse proposalResponse = client.sendProcessProposal(signedProposal);
        String message = proposalResponse.getResponseOrBuilder().getMessage();
//        log.info(proposalResponse.getPayload().toString());
//        String s = proposalResponse.getResponse().getPayload().toStringUtf8();
//        log.info(s);
        try {
            SmartContractShim.SmartContractMessage smartContractMessage = SmartContractShim.SmartContractMessage.parseFrom(proposalResponse.getResponse().getPayload());

            // log.info(smartContractMessage.toString());
            String s = smartContractMessage.getTimestamp().getUnknownFields().toString();
            String[] ss = s.split("1: 200\n" + "  2: \"");
            String[] sss = ss[1].split("\"\n" + "}\n");
            message = sss[0];
            message = message.replace("\\", "");


//            ProposalResponsePackage.Response response = ProposalResponsePackage.Response.parseFrom(smartContractMessage.getPayload());
//            log.info(response.getMessage());
//            message = response.getMessage();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        log.info("Query Result: " + message);
//                .getPayload().toStringUtf8());
        return message;
    }
}
