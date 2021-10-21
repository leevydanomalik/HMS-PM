package com.bitozen.hms.pm.core.employmentletter;

import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterChangeCommand;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterCreateCommand;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterDeleteCommand;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterStateAndEmploymentLetterStatusChangeCommand;
import com.bitozen.hms.pm.common.EmploymentLetterState;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterChangeEvent;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterCreateEvent;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterDeleteEvent;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterStateAndEmploymentLetterStatusChangeEvent;
import java.util.Date;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

/**
 *
 * @author Jeremia
 */
@Aggregate
public class EmploymentLetter {

    @AggregateIdentifier
    private String elID;
    private String elDocNumber;
    private String elDocURL;
    private EmploymentLetterState elState;
    private EmploymentLetterStatus elStatus;
    private Boolean isFinalApproval;
    private String reason;
    private Date reqDate;
    private String reqType;
    private String requestor;
    private String visaSpec;
    private String metadata;
    private String token;

    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String recordID;

    public EmploymentLetter() {

    }

    @CommandHandler
    public EmploymentLetter(EmploymentLetterCreateCommand command) {
        AggregateLifecycle.apply(new EmploymentLetterCreateEvent(
                command.getCreatedBy(),
                command.getCreatedDate(),
                command.getRecordID(),
                command.getElID(),
                command.getElDocNumber(),
                command.getElDocURL(),
                command.getElState(),
                command.getElStatus(),
                command.getIsFinalApproval(),
                command.getReason(),
                command.getReqDate(),
                command.getReqType(),
                command.getRequestor(),
                command.getVisaSpec(),
                command.getMetadata(),
                command.getToken()
        ));
    }

    @CommandHandler
    public void handle(EmploymentLetterChangeCommand command) {
        AggregateLifecycle.apply(new EmploymentLetterChangeEvent(
                command.getUpdatedBy(),
                command.getUpdatedDate(),
                command.getElID(),
                command.getElDocNumber(),
                command.getElDocURL(),
                command.getElState(),
                command.getElStatus(),
                command.getIsFinalApproval(),
                command.getReason(),
                command.getReqDate(),
                command.getReqType(),
                command.getRequestor(),
                command.getVisaSpec(),
                command.getMetadata(),
                command.getToken()
        ));
    }

    @CommandHandler
    public void handle(EmploymentLetterStateAndEmploymentLetterStatusChangeCommand command) {
        AggregateLifecycle.apply(new EmploymentLetterStateAndEmploymentLetterStatusChangeEvent(
                command.getUpdatedBy(),
                command.getUpdatedDate(),
                command.getElID(),
                command.getElState(),
                command.getElStatus(),
                command.getIsFinalApproval()
        ));
    }

    @CommandHandler
    public void handle(EmploymentLetterDeleteCommand command) {
        AggregateLifecycle.apply(new EmploymentLetterDeleteEvent(
                command.getElID(),
                command.getUpdatedBy()
        ));
    }

    @EventSourcingHandler
    public void on(EmploymentLetterCreateEvent event) {
        this.elID = event.getElID();
        this.elDocNumber = event.getElDocNumber();
        this.elDocURL = event.getElDocURL();
        this.elState = event.getElState();
        this.elStatus = event.getElStatus();
        this.isFinalApproval = event.getIsFinalApproval();
        this.reason = event.getReason();
        this.reqDate = event.getReqDate();
        this.reqType = event.getReqType();
        this.requestor = event.getRequestor();
        this.visaSpec = event.getVisaSpec();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.createdBy = event.getCreatedBy();
        this.createdDate = event.getCreatedDate();
        this.recordID = event.getRecordID();
    }

    @EventSourcingHandler
    public void on(EmploymentLetterChangeEvent event) {
        this.elID = event.getElID();
        this.elDocNumber = event.getElDocNumber();
        this.elDocURL = event.getElDocURL();
        this.elState = event.getElState();
        this.elStatus = event.getElStatus();
        this.isFinalApproval = event.getIsFinalApproval();
        this.reason = event.getReason();
        this.reqDate = event.getReqDate();
        this.reqType = event.getReqType();
        this.requestor = event.getRequestor();
        this.visaSpec = event.getVisaSpec();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.updatedBy = event.getUpdatedBy();
        this.updatedDate = event.getUpdatedDate();
    }

    @EventSourcingHandler
    public void on(EmploymentLetterStateAndEmploymentLetterStatusChangeEvent event) {
        this.elID = event.getElID();
        this.elState = event.getElState();
        this.elStatus = event.getElStatus();
        this.isFinalApproval = event.getIsFinalApproval();
        this.updatedBy = event.getUpdatedBy();
        this.updatedDate = event.getUpdatedDate();
    }

    @EventSourcingHandler
    public void on(EmploymentLetterDeleteEvent event) {
        this.elID = event.getElID();
        this.updatedBy = event.getUpdatedBy();
    }

}
