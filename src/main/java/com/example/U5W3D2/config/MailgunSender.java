package com.example.U5W3D2.config;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailgunSender {
    private String mailgunApikey;
    private String mailgunDomainName;

    public MailgunSender(@Value("${mailgun.apikey}") String mailgunApikey,
                         @Value("${mailgun.domainname}") String mailgunDomainName) {
        this.mailgunApikey = mailgunApikey;
        this.mailgunDomainName = mailgunDomainName;
    }

    public void sendMail(String recipient, @Value("${mail.from}") String mailFrom) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.mailgunDomainName + "/messages")
                .basicAuth("api", this.mailgunApikey)
                .queryString("from", mailFrom)
                .queryString("to", recipient)
                .queryString("subject", "Registrazione completata!")
                .queryString("text", "Grazie per averci scelto.")
                .asJson();
        System.out.println("Mail mandata dall'indirizzo" + mailFrom + "all'indirizzo " + recipient);
    }

    public void sendRemovedUserMail(String recipient, @Value("${mail.from}") String mailFrom) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.mailgunDomainName + "/messages")
                .basicAuth("api", this.mailgunApikey)
                .queryString("from", mailFrom)
                .queryString("to", recipient)
                .queryString("subject", "Account eliminato")
                .queryString("text", "Ci dispiace vederti andar via...")
                .asJson();
        System.out.println("Mail mandata dall'indirizzo" + mailFrom + "all'indirizzo " + recipient);
    }
}
