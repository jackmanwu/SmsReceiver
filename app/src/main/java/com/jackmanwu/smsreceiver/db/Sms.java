package com.jackmanwu.smsreceiver.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sms {
    private Integer id;
    private Integer smsId;
    private Integer state;
    private Integer createTime;
}
