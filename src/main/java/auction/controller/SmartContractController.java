package auction.controller;

import auction.model.Massage;
import auction.model.MoveModel;
import auction.operations.NodeSmartContract;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
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

    @RequestMapping("/jump")
    public ModelAndView jump(HttpServletRequest request){

       String type=request.getParameter("type");

        return new ModelAndView(type);
    }

    @RequestMapping("/instantiate")
    public ModelAndView instantiate(HttpServletRequest request){

        MoveModel move=new MoveModel();
        move.setTransfer(request.getParameter("transfer"));
        move.setPayee(request.getParameter("payee"));
        move.setMoney(request.getParameter("money"));

        String queryctor = "{\"args\":['query','a']}";

        JSONObject queryctorJson = JSONObject.parseObject(queryctor);

        JSONArray queryctorJSONArray = queryctorJson.getJSONArray("args");

        SmartContractPackage.SmartContractInput queryinput = null;


        SmartContractPackage.SmartContractInput.Builder queryinputBuilder = SmartContractPackage.SmartContractInput.newBuilder();

        for (int i = 0; i < queryctorJSONArray.size(); i++) {
            queryinputBuilder.addArgs(ByteString.copyFrom(queryctorJSONArray.getString(i).getBytes()));
        }

        queryinput = queryinputBuilder.build();
        Massage message=new Massage();
        try {
            NodeSmartContract nodeSmartContract = new NodeSmartContract(Node.getInstance());
            //message.setMassage(nodeSmartContract.query("myGroup","mycc",queryinput));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView view = new ModelAndView("result");
        view.addObject(message);
        return view;
    }


    @RequestMapping("/invoke")
    public ModelAndView invoke(HttpServletRequest request){

        MoveModel move=new MoveModel();
        move.setTransfer(request.getParameter("transfer"));
        move.setPayee(request.getParameter("payee"));
        move.setMoney(request.getParameter("money"));

        String invokector = "{'args':['move','"+move.getTransfer()+"','"+move.getPayee()+"','"+move.getMoney()+"']}";

        JSONObject invokectorJson = JSONObject.parseObject(invokector);

        JSONArray invokeargsJSONArray = invokectorJson.getJSONArray("args");

        SmartContractPackage.SmartContractInput invokeinput = null;

        SmartContractPackage.SmartContractInput.Builder invokeinputBuilder = SmartContractPackage.SmartContractInput.newBuilder();
        for (int i = 0; i < invokeargsJSONArray.size(); i++) {
            invokeinputBuilder.addArgs(ByteString.copyFrom(invokeargsJSONArray.getString(i).getBytes()));
        }

        invokeinput = invokeinputBuilder.build();
        Massage message0 = new Massage();
        String message=null;
        //Massage message=new Massage();
        try {
            NodeSmartContract nodeSmartContract = new NodeSmartContract(Node.getInstance());
            //nodeSmartContract.instantiate("192.168.1.164",7050,"myGroup","mycc","1.0",initinput);
            message=nodeSmartContract.invoke("192.168.1.155",7050,"myGroup","mycc","1.0",invokeinput);
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView view=null;
        if(message.equals("success")){
            view = new ModelAndView("result2");
            view.addObject(move);
        }
        else{
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
            String queryResponseStr = nodeSmartContract.query("myGroup", "mycc", queryinput);

            //JSONObject Json = JSONObject.parseObject(queryResponseStr);

            //JSONArray a = JSONArray.parseArray(queryResponseStr);
            //message.setMassage(queryResponseStr);
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
}
