package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.pm.common.dto.query.movement.MVPKWTDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVEmployeeCreateDTO implements Serializable {

    private String mvDetailID;
    private String employee;
    private List<MVSKCreateDTO> sks;
    private List<MVMemoCreateDTO> memos;
    private List<MVPKWTDTO> pkwts;
}
