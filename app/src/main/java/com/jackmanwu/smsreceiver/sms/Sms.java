package com.jackmanwu.smsreceiver.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sms {
    private Integer id;
    private Integer address;
    private Long date;
    private Long dateSent;
    private String body;
}
