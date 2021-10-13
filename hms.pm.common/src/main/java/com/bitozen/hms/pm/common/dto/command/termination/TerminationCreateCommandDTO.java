package com.bitozen.hms.pm.common.dto.command.termination;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.BAGPensionDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class TerminationCreateCommandDTO extends BAGTMNCreateSpec implements Serializable {

    private String tmnID;
    private String tmnNotes;
    private String bpjsHTDocNumber;
    private String bpjsPensionDocNumber;
    private List<String> docCopies;
    private Boolean isCancelFinalApprove;
    private Boolean isExecuted;
    private Boolean isFinalApprove;
    private String memoDocNumber;
    private String employee;
    private String requestor;
    private String skDocNumber;
    private String skdtDocNumber;
    private List<TerminationDocumentCreateDTO> tmnDocs;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnEffectiveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnReqDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnPphEndDate;
    private String tmnReason;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String recordID;

    public TerminationCreateCommandDTO(String tmnID, String tmnNotes, String bpjsHTDocNumber, String bpjsPensionDocNumber, List<String> docCopies, Boolean isCancelFinalApprove, Boolean isExecuted, Boolean isFinalApprove, String memoDocNumber, String employee, String requestor, String skDocNumber, String skdtDocNumber, List<TerminationDocumentCreateDTO> tmnDocs, Date tmnEffectiveDate, Date tmnReqDate, Date tmnPphEndDate, String tmnReason, TerminationState tmnState, TerminationStatus tmnStatus, MetadataCreateDTO metadata, GenericAccessTokenDTO token, String createdBy, Date createdDate, String recordID, BAGPensionDTO bagPensionSpec, BAGProlongedIllnessCreateDTO bagProlongedIllnessSpec) {
        super(bagPensionSpec, bagProlongedIllnessSpec);
        this.tmnID = tmnID;
        this.tmnNotes = tmnNotes;
        this.bpjsHTDocNumber = bpjsHTDocNumber;
        this.bpjsPensionDocNumber = bpjsPensionDocNumber;
        this.docCopies = docCopies;
        this.isCancelFinalApprove = isCancelFinalApprove;
        this.isExecuted = isExecuted;
        this.isFinalApprove = isFinalApprove;
        this.memoDocNumber = memoDocNumber;
        this.employee = employee;
        this.requestor = requestor;
        this.skDocNumber = skDocNumber;
        this.skdtDocNumber = skdtDocNumber;
        this.tmnDocs = tmnDocs;
        this.tmnEffectiveDate = tmnEffectiveDate;
        this.tmnReqDate = tmnReqDate;
        this.tmnPphEndDate = tmnPphEndDate;
        this.tmnReason = tmnReason;
        this.tmnState = tmnState;
        this.tmnStatus = tmnStatus;
        this.metadata = metadata;
        this.token = token;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.recordID = recordID;
    }
    
    
    

    @JsonIgnore
    public TerminationCreateCommandDTO getInstance() {
        List<String> listStrings = new ArrayList<>();
        listStrings.add("doc1");
        listStrings.add("doc2");
        return new TerminationCreateCommandDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "notes",
                "no bpjs",
                "no pensiun bpjs",
                listStrings,
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.FALSE,
                "memo doc number",
                "emp id",
                "emp id",
                "sk doc number",
                "skdt doc number",
                new ArrayList<>(Arrays.asList(new TerminationDocumentCreateDTO().getInstance())),
                new Date(),
                new Date(),
                new Date(),
                "KEY001",
                TerminationState.INITIATE,
                TerminationStatus.INITIATE,
                new MetadataCreateDTO().getInstance(),
                new GenericAccessTokenDTO(),
                "SYSTEM",
                new Date(),
                UUID.randomUUID().toString(),
                new BAGPensionDTO(),
                new BAGProlongedIllnessCreateDTO().getInstance()
        );

    }

}
