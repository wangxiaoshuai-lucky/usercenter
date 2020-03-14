package com.kelab.usercenter.support;

import com.kelab.usercenter.config.AppSetting;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.lang.NonNull;

import java.util.List;

public class MailSender {

    public static boolean send(@NonNull List<String> addresses, @NonNull String subject, @NonNull String content) {
        try {
            String host = AppSetting.mailHost;
            String username = AppSetting.mailUsername;
            String password = AppSetting.mailPassword;
            String sender = AppSetting.mailSender;
            String from = sender + "<" + username + ">";
            HtmlEmail email = new HtmlEmail();
            email.setHostName(host);
            email.setAuthenticator(new DefaultAuthenticator(username, password));
            email.setSSLOnConnect(true);
            email.setCharset("UTF-8");
            email.setFrom(from);
            email.setSubject(subject);
            email.setHtmlMsg(content);
            for (String address : addresses) {
                email.addTo(address);
            }
            email.send();
            return true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

}
