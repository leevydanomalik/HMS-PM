package com.bitozen.hms.pm.core.termination;

import com.bitozen.hms.pm.command.termination.TerminationChangeCommand;
import com.bitozen.hms.pm.command.termination.TerminationCreateCommand;
import com.bitozen.hms.pm.command.termination.TerminationDeleteCommand;
import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.event.termination.TerminationChangeEvent;
import com.bitozen.hms.pm.event.termination.TerminationCreateEvent;
import com.bitozen.hms.pm.event.termination.TerminationDeleteEvent;
import java.util.Date;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class Termination {
    
    @AggregateIdentifier
    private String tmnID;
    private String tmnNotes;
    private String bpjsHTDocNumber;
    private String bpjsPensionDocNumber;
    private String docCopies;
    private Boolean isCancelFinalApprove;
    private Boolean isExecuted;
    private Boolean isFinalApprove;
    private String memoDocNumber;
    private String employee;
    private String requestor;
    private String skDocNumber;
    private String skdtDocNumber;
    private String tmnDocs;
    private Date tmnEffectiveDate;
    private Date tmnReqDate;
    private Date tmnPphEndDate;
    private String tmnReason;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private String metadata;
    private String token;
    private String bagPensionSpec;
    private String bagProlongedIllnessSpec;
    
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String recordID;
    
    public Termination(){
    
    }
    
    
    @CommandHandler
    public Termination(TerminationCreateCommand command) {
        AggregateLifecycle.apply(new TerminationCreateEvent(
                command.getTmnID(),
                command.getTmnNotes(),
                command.getBpjsHTDocNumber(),
                command.getBpjsPensionDocNumber(),
                command.getDocCopies(),
                command.getIsCancelFinalApprove(),
                command.getIsExecuted(),
                command.getIsFinalApprove(),
                command.getMemoDocNumber(),
                command.getEmployee(),
                command.getRequestor(),
                command.getSkDocNumber(),
                command.getSkdtDocNumber(),
                command.getTmnDocs(),
                command.getTmnEffectiveDate(),
                command.getTmnReqDate(),
                command.getTmnPphEndDate(),
                command.getTmnReason(),
                command.getTmnState(),
                command.getTmnStatus(),
                command.getMetadata(),
                command.getToken(),
                command.getBagPensionSpec(),
                command.getBagProlongedIllnessSpec(),
                command.getCreatedBy(),
                command.getCreatedDate(),
                command.getRecordID()
        ));

    }
    
    @CommandHandler
    public void handle(TerminationChangeCommand command) {
        AggregateLifecycle.apply(new TerminationChangeEvent(
                command.getTmnID(),
                command.getTmnNotes(),
                command.getBpjsHTDocNumber(),
                command.getBpjsPensionDocNumber(),
                command.getDocCopies(),
                command.getIsCancelFinalApprove(),
                command.getIsExecuted(),
                command.getIsFinalApprove(),
                command.getMemoDocNumber(),
                command.getEmployee(),
                command.getRequestor(),
                command.getSkDocNumber(),
                command.getSkdtDocNumber(),
                command.getTmnDocs(),
                command.getTmnEffectiveDate(),
                command.getTmnReqDate(),
                command.getTmnPphEndDate(),
                command.getTmnReason(),
                command.getTmnState(),
                command.getTmnStatus(),
                command.getMetadata(),
                command.getToken(),
                command.getBagPensionSpec(),
                command.getBagProlongedIllnessSpec(),
                command.getUpdatedBy(),
                command.getUpdatedDate()
        ));

    }
    
    @CommandHandler
    public void handle(TerminationDeleteCommand command) {
        AggregateLifecycle.apply(new TerminationDeleteEvent(
                command.getTmnID(),
                command.getUpdatedBy()
        ));

    }
    
    @EventSourcingHandler
    public void on(TerminationCreateEvent event){
        this.tmnID = event.getTmnID();
        this.tmnNotes = event.getTmnNotes();
        this.bpjsHTDocNumber = event.getBpjsHTDocNumber();
        this.bpjsPensionDocNumber = event.getBpjsPensionDocNumber();
        this.docCopies = event.getDocCopies();
        this.isCancelFinalApprove = event.getIsCancelFinalApprove();
        this.isExecuted = event.getIsExecuted();
        this.isFinalApprove = event.getIsFinalApprove();
        this.memoDocNumber = event.getMemoDocNumber();
        this.employee = event.getEmployee();
        this.requestor = event.getRequestor();
        this.skDocNumber = event.getSkDocNumber();
        this.skdtDocNumber = event.getSkdtDocNumber();
        this.tmnDocs = event.getTmnDocs();
        this.tmnEffectiveDate = event.getTmnEffectiveDate();
        this.tmnReqDate = event.getTmnReqDate();
        this.tmnPphEndDate = event.getTmnPphEndDate();
        this.tmnReason = event.getTmnReason();
        this.tmnState = event.getTmnState();
        this.tmnStatus = event.getTmnStatus();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.bagPensionSpec = event.getBagPensionSpec();
        this.bagProlongedIllnessSpec = event.getBagProlongedIllnessSpec();
        this.createdBy = event.getCreatedBy();
        this.createdDate = event.getCreatedDate();
        this.recordID = event.getRecordID();
    
    }
    
    @EventSourcingHandler
    public void on(TerminationChangeEvent event){
        this.tmnID = event.getTmnID();
        this.tmnNotes = event.getTmnNotes();
        this.bpjsHTDocNumber = event.getBpjsHTDocNumber();
        this.bpjsPensionDocNumber = event.getBpjsPensionDocNumber();
        this.docCopies = event.getDocCopies();
        this.isCancelFinalApprove = event.getIsCancelFinalApprove();
        this.isExecuted = event.getIsExecuted();
        this.isFinalApprove = event.getIsFinalApprove();
        this.memoDocNumber = event.getMemoDocNumber();
        this.employee = event.getEmployee();
        this.requestor = event.getRequestor();
        this.skDocNumber = event.getSkDocNumber();
        this.skdtDocNumber = event.getSkdtDocNumber();
        this.tmnDocs = event.getTmnDocs();
        this.tmnEffectiveDate = event.getTmnEffectiveDate();
        this.tmnReqDate = event.getTmnReqDate();
        this.tmnPphEndDate = event.getTmnPphEndDate();
        this.tmnReason = event.getTmnReason();
        this.tmnState = event.getTmnState();
        this.tmnStatus = event.getTmnStatus();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.bagPensionSpec = event.getBagPensionSpec();
        this.bagProlongedIllnessSpec = event.getBagProlongedIllnessSpec();
        this.updatedBy = event.getUpdatedBy();
        this.updatedDate = event.getUpdatedDate();
    
    }
    
    @EventSourcingHandler
    public void on(TerminationDeleteEvent event){
        this.tmnID = event.getTmnID();
        this.updatedBy = event.getUpdatedBy();
    
    }
    
    
    
    
}
