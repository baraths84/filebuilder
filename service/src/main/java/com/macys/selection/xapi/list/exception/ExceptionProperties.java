package com.macys.selection.xapi.list.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:list-exception.properties")
public class ExceptionProperties {

    @Value("${error_10147_message}")
    private String message10147;

    @Value("${error_10148_message}")
    private String message10148;

    @Value("${error_10108_message}")
    private String message10108;

    @Value("${error_10109_message}")
    private String message10109;

    @Value("${service_failure_message}")
    private String serviceFailureMessage;

    @Value("${temporary_failure_message}")
    private String temporaryFailureMessage;

    @Value("${error_50001_message}")
    private String message50001;

    @Value("${error_10156_message}")
    private String message10156;

    @Value("${error_10101_message}")
    private String message10101;

    @Value("${error_10111_message}")
    private String message10111;

    @Value("${error_10103_message}")
    private String message10103;

    @Value("${error_10119_message}")
    private String message10119;

    @Value("${error_10110_message}")
    private String message10110;

    @Value("${error_10139_message}")
    private String message10139;

    @Value("${error_10151_message}")
    private String message10151;

    @Value("${service_failure_message_bcom}")
    private String serviceFailureMessageForBcom;

    @Value("${error_50001_message_bcom}")
    private String message50001Bcom;

    public String getMessage10147() {
        return message10147;
    }

    public void setMessage10147(String message10147) {
        this.message10147 = message10147;
    }

    public String getMessage10148() {
        return message10148;
    }

    public void setMessage10148(String message10148) {
        this.message10148 = message10148;
    }

    public String getMessage10108() {
        return message10108;
    }

    public void setMessage10108(String message10108) {
        this.message10108 = message10108;
    }

    public String getMessage10109() {
        return message10109;
    }

    public void setMessage10109(String message10109) {
        this.message10109 = message10109;
    }

    public String getServiceFailureMessage() {
        return serviceFailureMessage;
    }

    public void setServiceFailureMessage(String serviceFailureMessage) {
        this.serviceFailureMessage = serviceFailureMessage;
    }

    public String getTemporaryFailureMessage() {
        return temporaryFailureMessage;
    }

    public void setTemporaryFailureMessage(String temporaryFailureMessage) {
        this.temporaryFailureMessage = temporaryFailureMessage;
    }

    public String getMessage50001() {
        return message50001;
    }

    public void setMessage50001(String message_50001) {
        this.message50001 = message_50001;
    }

    public String getMessage10156() {
        return message10156;
    }

    public void setMessage10156(String message_10156) {
        this.message10156 = message_10156;
    }

    public String getMessage10101() {
        return message10101;
    }

    public void setMessage10101(String message10101) {
        this.message10101 = message10101;
    }

    public String getMessage10111() {
        return message10111;
    }

    public void setMessage10111(String message10111) {
        this.message10111 = message10111;
    }

    public String getMessage10103() {
        return message10103;
    }

    public void setMessage10103(String message10103) {
        this.message10103 = message10103;
    }

    public String getMessage10119() {
        return message10119;
    }

    public void setMessage10119(String message10119) {
        this.message10119 = message10119;
    }

    public String getMessage10110() {
        return message10110;
    }

    public void setMessage10110(String message10110) {
        this.message10110 = message10110;
    }

    public String getMessage10139() {
        return message10139;
    }

    public void setMessage10139(String message10139) {
        this.message10139 = message10139;
    }

    public String getMessage10151() {
        return message10151;
    }

    public void setMessage10151(String message10151) {
        this.message10151 = message10151;
    }

    public String getServiceFailureMessageForBcom() {
        return serviceFailureMessageForBcom;
    }

    public void setServiceFailureMessageForBcom(String serviceFailureMessageForBcom) {
        this.serviceFailureMessageForBcom = serviceFailureMessageForBcom;
    }

    public String getMessage50001Bcom() {
        return message50001Bcom;
    }

    public void setMessage50001Bcom(String message50001Bcom) {
        this.message50001Bcom = message50001Bcom;
    }
}
