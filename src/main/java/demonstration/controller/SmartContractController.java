package demonstration.controller;

import demonstration.model.BlockInfo;
import demonstration.model.Massage;
import demonstration.model.MoveModel;
import demonstration.model.Point;
import demonstration.operations.NodeSmartContract;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import org.bcia.julongchain.common.exception.NodeException;
import org.bcia.julongchain.node.Node;
import org.bcia.julongchain.protos.node.SmartContractPackage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lishaojie
 * @Date: 2018/7/6
 * @company Dingxuan
 */

@Controller
@RequestMapping("/sc")
public class SmartContractController {

//    private String nodeHost = "192.168.1.155";
//    private int nodePort = 10051;
//    private String point.getConsenterHost() = "192.168.1.155";
//    private int consenterPort = 7050;

    private Point point = new Point();

    @RequestMapping("/jump")
    public ModelAndView jump(HttpServletRequest request) {

        String type = request.getParameter("type");

        if (type == null) {
            type = "main";
        }
        if (type.equals("block")) {
            ModelAndView block = new ModelAndView("showBlock");
            BlockInfo blockInfo = block();
            block.addObject(blockInfo);
            return block;
        }
        if (type.equals("point")) {
            ModelAndView a = new ModelAndView("point");
            a.addObject(point);
            return a;
        }
        return new ModelAndView(type);
    }

    @RequestMapping("/invoke")
    public ModelAndView invoke(HttpServletRequest request) {

        MoveModel move = new MoveModel();
        move.setTransfer(request.getParameter("transfer"));
        move.setPayee(request.getParameter("payee"));
        move.setMoney(request.getParameter("money"));

        String invokector = "{'args':['move','" + move.getTransfer() + "','" + move.getPayee() + "','" + move.getMoney() + "']}";

        JSONObject invokectorJson = JSONObject.parseObject(invokector);

        JSONArray invokeargsJSONArray = invokectorJson.getJSONArray("args");

        SmartContractPackage.SmartContractInput invokeinput = null;

        SmartContractPackage.SmartContractInput.Builder invokeinputBuilder = SmartContractPackage.SmartContractInput.newBuilder();
        for (int i = 0; i < invokeargsJSONArray.size(); i++) {
            invokeinputBuilder.addArgs(ByteString.copyFrom(invokeargsJSONArray.getString(i).getBytes()));
        }

        invokeinput = invokeinputBuilder.build();
        Massage message0 = new Massage();
        String message = null;
        //Massage message=new Massage();
        try {
            NodeSmartContract nodeSmartContract = new NodeSmartContract(Node.getInstance());
            message = nodeSmartContract.invoke(point.getConsenterHost(), point.getConsenterPort(), point.getNodeHost(), point.getNodePort(), "myGroup", "mycc", "1.0", invokeinput);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView view = null;
        if (message.equals("success")) {
            view = new ModelAndView("result2");
            view.addObject(move);
        } else {
            view = new ModelAndView("error");
            message0.setValue(message);
            view.addObject(message0);
        }

        return view;
    }

    @RequestMapping("/query")
    public ModelAndView query(HttpServletRequest request) {

        MoveModel move = new MoveModel();
        move.setTransfer(request.getParameter("transfer"));
        move.setPayee(request.getParameter("payee"));
        move.setMoney(request.getParameter("money"));

        String name = request.getParameter("name");
        String queryctor = "{'args':['query','" + name + "']}";

        JSONObject queryctorJson = JSONObject.parseObject(queryctor);

        JSONArray queryctorJSONArray = queryctorJson.getJSONArray("args");

        SmartContractPackage.SmartContractInput queryinput = null;


        SmartContractPackage.SmartContractInput.Builder queryinputBuilder = SmartContractPackage.SmartContractInput.newBuilder();
        for (int i = 0; i < queryctorJSONArray.size(); i++) {
            queryinputBuilder.addArgs(ByteString.copyFrom(queryctorJSONArray.getString(i).getBytes()));
        }

        queryinput = queryinputBuilder.build();
        Massage message = new Massage();
        List<Massage> Massage = null;
        try {
            NodeSmartContract nodeSmartContract = new NodeSmartContract(Node.getInstance());
            String queryResponseStr = nodeSmartContract.query(point.getNodeHost(), point.getNodePort(), "myGroup", "mycc", queryinput);

            Massage = JSON.parseArray(queryResponseStr, Massage.class);
            message.setName(Massage.get(0).getName());
            message.setValue(Massage.get(0).getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView view = new ModelAndView("result");
        view.addObject(message);
        return view;
    }

    public BlockInfo block() {
        NodeSmartContract nodeSmartContract = null;

        String ctor = "{'args':['GetGroupInfo','myGroup']}";

        JSONObject ctorJson = JSONObject.parseObject(ctor);

        JSONArray ctorJSONArray = ctorJson.getJSONArray("args");

        SmartContractPackage.SmartContractInput input = null;

        long height = 3;
        SmartContractPackage.SmartContractInput.Builder inputBuilder = SmartContractPackage.SmartContractInput.newBuilder();
        for (int i = 0; i < ctorJSONArray.size(); i++) {
            inputBuilder.addArgs(ByteString.copyFrom(ctorJSONArray.getString(i).getBytes()));
        }
        input = inputBuilder.build();
        try {
            nodeSmartContract = new NodeSmartContract(Node.getInstance());
            height = nodeSmartContract.height( point.getNodeHost(), point.getNodePort(), "myGroup", "qssc", input);
        } catch (NodeException e) {
            e.printStackTrace();
        }

        String bn = String.valueOf(height - 1);

        String queryctor = "{'args':['GetBlockByNumber','myGroup','" + bn + "']}";

        JSONObject queryctorJson = JSONObject.parseObject(queryctor);

        JSONArray queryctorJSONArray = queryctorJson.getJSONArray("args");

        SmartContractPackage.SmartContractInput queryinput = null;

        SmartContractPackage.SmartContractInput.Builder queryinputBuilder = SmartContractPackage.SmartContractInput.newBuilder();
        for (int i = 0; i < queryctorJSONArray.size(); i++) {
            queryinputBuilder.addArgs(ByteString.copyFrom(queryctorJSONArray.getString(i).getBytes()));
        }
        BlockInfo blockInfo = new BlockInfo();
        queryinput = queryinputBuilder.build();

        try {
            // NodeSmartContract nodeSmartContract = new NodeSmartContract(Node.getInstance());
            String queryResponseStr = nodeSmartContract.block(point.getNodeHost(), point.getNodePort(), "myGroup", "qssc", queryinput);
            blockInfo = JSON.parseObject(queryResponseStr, BlockInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blockInfo;
    }

    @RequestMapping("/point")
    public ModelAndView point(HttpServletRequest request) {
        point.setNodeHost(request.getParameter("nodeHost"));
        point.setNodePort(Integer.parseInt(request.getParameter("nodePort")));
        point.setConsenterHost(request.getParameter("consenterHost"));
        point.setConsenterPort(Integer.parseInt(request.getParameter("consenterPort")));

        ModelAndView view = new ModelAndView("main");
        //view.addObject(block);
        return view;
    }

}
