package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
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
public class MVEmployeeDTO implements Serializable {

    private String mvDetailID;
    private EmployeeOptimizeDTO employee;
    private List<MVSKDTO> sks;
    private List<MVMemoDTO> memos;
    private List<MVPKWTDTO> pkwts;
}
