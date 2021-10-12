package com.bitozen.hms.pm.core.blacklist;

import com.bitozen.hms.pm.command.blacklist.BlacklistChangeCommand;
import com.bitozen.hms.pm.command.blacklist.BlacklistCreateCommand;
import com.bitozen.hms.pm.command.blacklist.BlacklistDeleteCommand;
import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import com.bitozen.hms.pm.event.blacklist.BlacklistChangeEvent;
import com.bitozen.hms.pm.event.blacklist.BlacklistCreateEvent;
import com.bitozen.hms.pm.event.blacklist.BlacklistDeleteEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate
public class Blacklist {

    @AggregateIdentifier
    private String blacklistID;
    private String blacklistSPKNumber;
    private Date blacklistStartDate;
    private Date blacklistEndDate;
    private String blacklistNotes;
    private String blacklistType;
    private Boolean isPermanent;
    private String employee;
    private String requestor;
    private String blacklistDocURL;
    private Boolean isFinalApprove;
    private BlacklistStatus blacklistStatus;
    private BlacklistState blacklistState;
    private String compRegulationChapter;
    private String compRegulationChapterDesc;
    private String metadata;
    private String token;

    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String recordID;

    public Blacklist() {

    }

    @CommandHandler
    public Blacklist(BlacklistCreateCommand command) {
        AggregateLifecycle.apply(new BlacklistCreateEvent(
                command.getCreatedBy(),
                command.getCreatedDate(),
                command.getRecordID(),
                command.getBlacklistID(),
                command.getBlacklistSPKNumber(),
                command.getBlacklistStartDate(),
                command.getBlacklistEndDate(),
                command.getBlacklistNotes(),
                command.getBlacklistType(),
                command.getIsPermanent(),
                command.getEmployee(),
                command.getRequestor(),
                command.getBlacklistDocURL(),
                command.getIsFinalApprove(),
                command.getBlacklistStatus(),
                command.getBlacklistState(),
                command.getCompRegulationChapter(),
                command.getCompRegulationChapterDesc(),
                command.getMetadata(),
                command.getToken()
                ));
    }

    @CommandHandler
    public void handle(BlacklistChangeCommand command) {
        AggregateLifecycle.apply(new BlacklistChangeEvent(
                command.getUpdatedBy(),
                command.getUpdatedDate(),
                command.getBlacklistID(),
                command.getBlacklistSPKNumber(),
                command.getBlacklistStartDate(),
                command.getBlacklistEndDate(),
                command.getBlacklistNotes(),
                command.getBlacklistType(),
                command.getIsPermanent(),
                command.getEmployee(),
                command.getRequestor(),
                command.getBlacklistDocURL(),
                command.getIsFinalApprove(),
                command.getBlacklistStatus(),
                command.getBlacklistState(),
                command.getCompRegulationChapter(),
                command.getCompRegulationChapterDesc(),
                command.getMetadata(),
                command.getToken()
        ));
    }

    @CommandHandler
    public void handle(BlacklistDeleteCommand command) {
        AggregateLifecycle.apply(new BlacklistDeleteEvent(
                command.getBlacklistID(),
                command.getUpdatedBy()
        ));
    }

    @EventSourcingHandler
    public void on(BlacklistCreateEvent event) {
        this.blacklistID = event.getBlacklistID();
        this.blacklistSPKNumber = event.getBlacklistSPKNumber();
        this.blacklistStartDate = event.getBlacklistStartDate();
        this.blacklistEndDate = event.getBlacklistEndDate();
        this.blacklistNotes = event.getBlacklistNotes();
        this.blacklistType = event.getBlacklistType();
        this.isPermanent = event.getIsPermanent();
        this.employee = event.getEmployee();
        this.requestor = event.getRequestor();
        this.blacklistDocURL= event.getBlacklistDocURL();
        this.isFinalApprove = event.getIsFinalApprove();
        this.blacklistStatus = event.getBlacklistStatus();
        this.blacklistState = event.getBlacklistState();
        this.compRegulationChapter = event.getCompRegulationChapter();
        this.compRegulationChapterDesc = event.getCompRegulationChapterDesc();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.createdBy = event.getCreatedBy();
        this.createdDate = event.getCreatedDate();
        this.recordID = event.getRecordID();
    }

    @EventSourcingHandler
    public void on(BlacklistChangeEvent event) {
        this.blacklistID = event.getBlacklistID();
        this.blacklistSPKNumber = event.getBlacklistSPKNumber();
        this.blacklistStartDate = event.getBlacklistStartDate();
        this.blacklistEndDate = event.getBlacklistEndDate();
        this.blacklistNotes = event.getBlacklistNotes();
        this.blacklistType = event.getBlacklistType();
        this.isPermanent = event.getIsPermanent();
        this.employee = event.getEmployee();
        this.requestor = event.getRequestor();
        this.blacklistDocURL= event.getBlacklistDocURL();
        this.isFinalApprove = event.getIsFinalApprove();
        this.blacklistStatus = event.getBlacklistStatus();
        this.blacklistState = event.getBlacklistState();
        this.compRegulationChapter = event.getCompRegulationChapter();
        this.compRegulationChapterDesc = event.getCompRegulationChapterDesc();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.updatedBy = event.getUpdatedBy();
        this.updatedDate = event.getUpdatedDate();
    }

    @EventSourcingHandler
    public void on(BlacklistDeleteEvent event) {
        this.blacklistID = event.getBlacklistID();
        this.updatedBy = event.getUpdatedBy();
    }
}