package com.demo.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信
 */
public class SendSmsDemo {

    /**
     * 设置鉴权参数，初始化客户端
     * （地域ID，您的AccessKey ID，您的AccessKey Secret）
     */
    private DefaultProfile profile = DefaultProfile.getProfile(
            "cn-hangzhou",
            "修改为您的AccessKey",
            "修改为您的AccessKey Secret");
    private IAcsClient client = new DefaultAcsClient(profile);

    private static void log_print(String functionName, Object result) {
        Gson gson = new Gson();
        System.out.println("-------------------------------" + functionName + "-------------------------------");
        System.out.println(gson.toJson(result));
    }

    /**
     * 添加短信模板（不用看这里，已经在控制台创建好了）
     */
    private String addSmsTemplate() throws ClientException {
        CommonRequest addSmsTemplateRequest = new CommonRequest();
        addSmsTemplateRequest.setSysDomain("dysmsapi.aliyuncs.com");
        addSmsTemplateRequest.setSysAction("AddSmsTemplate");
        addSmsTemplateRequest.setSysVersion("2017-05-25");
        // 短信类型。0：验证码；1：短信通知；2：推广短信；3：国际/港澳台消息
        addSmsTemplateRequest.putQueryParameter("TemplateType", "0");
        // 模板名称，长度为1~30个字符
        addSmsTemplateRequest.putQueryParameter("TemplateName", "测试短信模板");
        // 模板内容，长度为1~500个字符
        addSmsTemplateRequest.putQueryParameter("TemplateContent", "您正在申请手机注册，验证码为：${code}，5分钟内有效！");
        // 短信模板申请说明
        addSmsTemplateRequest.putQueryParameter("Remark", "测试");
        CommonResponse addSmsTemplateResponse = client.getCommonResponse(addSmsTemplateRequest);
        String data = addSmsTemplateResponse.getData();
        // 消除返回文本中的反转义字符
        String sData = data.replaceAll("'\'", "");
        log_print("addSmsTemplate", sData);
        Gson gson = new Gson();
        // 将字符串转换为Map类型，取TemplateCode字段值
        Map map = gson.fromJson(sData, Map.class);
        Object templateCode = map.get("TemplateCode");
        return templateCode.toString();
    }

    /**
     * 发送短信
     */
    private String sendSms(String telephone, String param) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        // 接收短信的手机号码
        request.putQueryParameter("PhoneNumbers", telephone);
        // 短信签名名称。请在控制台签名管理页面签名名称一列查看（必须是已添加、并通过审核的短信签名）。
        request.putQueryParameter("SignName", "换成您的短信签名");
        // 短信模板ID
        request.putQueryParameter("TemplateCode", "换成您的短信模板ID");
        // 短信模板变量对应的实际值，JSON格式。
        request.putQueryParameter("TemplateParam", param);
        CommonResponse commonResponse = client.getCommonResponse(request);
        String data = commonResponse.getData();
        String sData = data.replaceAll("'\'", "");
        log_print("sendSms", sData);
        Gson gson = new Gson();
        Map map = gson.fromJson(sData, Map.class);
        Object bizId = map.get("BizId");
        return bizId.toString();
    }

    /**
     * 查询发送详情
     */
    private void querySendDetails(String bizId, String telephone) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("QuerySendDetails");
        // 接收短信的手机号码
        request.putQueryParameter("PhoneNumber", telephone);
        // 短信发送日期，支持查询最近30天的记录。格式为yyyyMMdd，例如20191010。
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        request.putQueryParameter("SendDate", today);
        // 分页记录数量
        request.putQueryParameter("PageSize", "10");
        // 分页当前页码
        request.putQueryParameter("CurrentPage", "1");
        // 发送回执ID，即发送流水号。
        request.putQueryParameter("BizId", bizId);
        CommonResponse response = client.getCommonResponse(request);
        log_print("querySendDetails", response.getData());
    }

    public static void main(String[] args) {
        SendSmsDemo sendSmsDemo = new SendSmsDemo();
        try {

            Map<String, String> map = new HashMap(1);
            //验证码信息
            map.put("code", "123456");
            Gson gson = new Gson();
            // 发送短信
            String bizId = sendSmsDemo.sendSms("换成接收短信的手机号", gson.toJson(map));
            // 根据短信发送流水号查询短信发送情况
            sendSmsDemo.querySendDetails(bizId, "换成接收短信的手机号");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}