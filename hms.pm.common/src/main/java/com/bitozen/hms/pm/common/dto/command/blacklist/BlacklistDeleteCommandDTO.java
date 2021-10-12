package com.bitozen.hms.pm.common.dto.command.blacklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlacklistDeleteCommandDTO implements Serializable {

    private String blacklistID;

    private String updatedBy;
}
