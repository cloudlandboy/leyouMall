package com.leyou.sms.listener;

import com.aliyuncs.CommonResponse;
import com.google.gson.JsonObject;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.util.SendSmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SendSmsUtils SendSmsUtils;

    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SMS.QUEUE", durable = "true"),
            exchange = @Exchange(value = "LEYOU.SMS.EXCHANGE", ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"})
    )
    public void listenSms(Map<String, String> msg) throws Exception {
        if (CollectionUtils.isEmpty(msg)) {
            // 放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if (StringUtils.isAnyBlank(phone, code)) {
            // 放弃处理
            return;
        }

        //发送消息
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        CommonResponse commonResponse = this.SendSmsUtils.sendSms(phone, jsonObject.toString(), smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate());

    }
}