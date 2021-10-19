package com.bitozen.hms.pm.core.movement;

import com.bitozen.hms.pm.command.movement.*;
import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.event.movement.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate
public class Movement {

    @AggregateIdentifier
    private String mvID;
    private String mvNotes;
    private Date mvSPKDocNumber;
    private Date mvEffectiveDate;
    private Boolean isFinalApprove;
    private MVStatus mvStatus;
    private MVState mvState;
    private String mvCase;
    private String mvType;
    private String requestor;
    private String assignment;
    private String employees;
    private String mvDocs;
    private String mvBenefitBefore;
    private String mvBenefitAfter;
    private String mvFacilityBefore;
    private String mvFacilityAfter;
    private String mvPayroll;
    private String mvPosition;
    private String refRecRequest;
    private String metadata;
    private String token;

    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String recordID;

    public Movement() {

    }

    @CommandHandler
    public Movement(MovementCreateCommand command) {
        AggregateLifecycle.apply(new MovementCreateEvent(
                command.getCreatedBy(),
                command.getCreatedDate(),
                command.getRecordID(),
                command.getMvID(),
                command.getMvNotes(),
                command.getMvSPKDocNumber(),
                command.getMvEffectiveDate(),
                command.getIsFinalApprove(),
                command.getMvStatus(),
                command.getMvState(),
                command.getMvCase(),
                command.getMvType(),
                command.getRequestor(),
                command.getAssignment(),
                command.getEmployees(),
                command.getMvDocs(),
                command.getMvBenefitBefore(),
                command.getMvBenefitAfter(),
                command.getMvFacilityBefore(),
                command.getMvFacilityAfter(),
                command.getMvPayroll(),
                command.getMvPosition(),
                command.getRefRecRequest(),
                command.getMetadata(),
                command.getToken()
        ));
    }

    @CommandHandler
    public void handle(MovementChangeCommand command) {
        AggregateLifecycle.apply(new MovementChangeEvent(
                command.getUpdatedBy(),
                command.getUpdatedDate(),
                command.getMvID(),
                command.getMvNotes(),
                command.getMvSPKDocNumber(),
                command.getMvEffectiveDate(),
                command.getIsFinalApprove(),
                command.getMvStatus(),
                command.getMvState(),
                command.getMvCase(),
                command.getMvType(),
                command.getRequestor(),
                command.getAssignment(),
                command.getEmployees(),
                command.getMvDocs(),
                command.getMvBenefitBefore(),
                command.getMvBenefitAfter(),
                command.getMvFacilityBefore(),
                command.getMvFacilityAfter(),
                command.getMvPayroll(),
                command.getMvPosition(),
                command.getRefRecRequest(),
                command.getMetadata(),
                command.getToken()
        ));
    }

    @CommandHandler
    public void handle(MovementDeleteCommand command) {
        AggregateLifecycle.apply(new MovementDeleteEvent(
                command.getMvID(),
                command.getUpdatedBy()
        ));
    }

    @CommandHandler
    public void handle(MovementSKCreateCommand command) {
        AggregateLifecycle.apply(new MovementSKCreateEvent(
                command.getMvID(),
                command.getEmployees()
        ));
    }

    @CommandHandler
    public void handle(MovementSKChangeCommand command) {
        AggregateLifecycle.apply(new MovementSKChangeEvent(
                command.getMvID(),
                command.getEmployees()
        ));
    }

    @CommandHandler
    public void handle(MovementSKDeleteCommand command) {
        AggregateLifecycle.apply(new MovementSKDeleteEvent(
                command.getMvID(),
                command.getEmployees()
        ));
    }

    @CommandHandler
    public void handle(MovementMemoCreateCommand command) {
        AggregateLifecycle.apply(new MovementMemoCreateEvent(
                command.getMvID(),
                command.getEmployees()
        ));
    }

    @CommandHandler
    public void handle(MovementMemoChangeCommand command) {
        AggregateLifecycle.apply(new MovementMemoChangeEvent(
                command.getMvID(),
                command.getEmployees()
        ));
    }

    @CommandHandler
    public void handle(MovementMemoDeleteCommand command) {
        AggregateLifecycle.apply(new MovementMemoDeleteEvent(
                command.getMvID(),
                command.getEmployees()
        ));
    }

    @EventSourcingHandler
    public void on(MovementCreateEvent event) {
        this.mvID = event.getMvID();
        this.mvNotes = event.getMvNotes();
        this.mvSPKDocNumber = event.getMvSPKDocNumber();
        this.mvEffectiveDate = event.getMvEffectiveDate();
        this.isFinalApprove = event.getIsFinalApprove();
        this.mvStatus = event.getMvStatus();
        this.mvState = event.getMvState();
        this.mvCase = event.getMvCase();
        this.mvType = event.getMvType();
        this.requestor = event.getRequestor();
        this.assignment = event.getAssignment();
        this.employees = event.getEmployees();
        this.mvDocs = event.getMvDocs();
        this.mvBenefitBefore = event.getMvBenefitBefore();
        this.mvBenefitAfter = event.getMvBenefitAfter();
        this.mvFacilityBefore = event.getMvFacilityBefore();
        this.mvFacilityAfter = event.getMvFacilityAfter();
        this.mvPayroll = event.getMvPayroll();
        this.mvPosition = event.getMvPosition();
        this.refRecRequest = event.getRefRecRequest();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.createdBy = event.getCreatedBy();
        this.createdDate = event.getCreatedDate();
        this.recordID = event.getRecordID();
    }

    @EventSourcingHandler
    public void on(MovementChangeEvent event) {
        this.mvID = event.getMvID();
        this.mvNotes = event.getMvNotes();
        this.mvSPKDocNumber = event.getMvSPKDocNumber();
        this.mvEffectiveDate = event.getMvEffectiveDate();
        this.isFinalApprove = event.getIsFinalApprove();
        this.mvStatus = event.getMvStatus();
        this.mvState = event.getMvState();
        this.mvCase = event.getMvCase();
        this.mvType = event.getMvType();
        this.requestor = event.getRequestor();
        this.assignment = event.getAssignment();
        this.employees = event.getEmployees();
        this.mvDocs = event.getMvDocs();
        this.mvBenefitBefore = event.getMvBenefitBefore();
        this.mvBenefitAfter = event.getMvBenefitAfter();
        this.mvFacilityBefore = event.getMvFacilityBefore();
        this.mvFacilityAfter = event.getMvFacilityAfter();
        this.mvPayroll = event.getMvPayroll();
        this.mvPosition = event.getMvPosition();
        this.refRecRequest = event.getRefRecRequest();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.updatedBy = event.getUpdatedBy();
        this.updatedDate = event.getUpdatedDate();
    }

    @EventSourcingHandler
    public void on(MovementDeleteEvent event) {
        this.mvID = event.getMvID();
        this.updatedBy = event.getUpdatedBy();
    }

    @EventSourcingHandler
    public void on(MovementSKCreateEvent event) {
        this.mvID = event.getMvID();
        this.employees = event.getEmployees();
    }

    @EventSourcingHandler
    public void on(MovementSKChangeEvent event) {
        this.mvID = event.getMvID();
        this.employees = event.getEmployees();
    }

    @EventSourcingHandler
    public void on(MovementSKDeleteEvent event) {
        this.mvID = event.getMvID();
        this.employees = event.getEmployees();
    }

    @EventSourcingHandler
    public void on(MovementMemoCreateEvent event) {
        this.mvID = event.getMvID();
        this.employees = event.getEmployees();
    }

    @EventSourcingHandler
    public void on(MovementMemoChangeEvent event) {
        this.mvID = event.getMvID();
        this.employees = event.getEmployees();
    }

    @EventSourcingHandler
    public void on(MovementMemoDeleteEvent event) {
        this.mvID = event.getMvID();
        this.employees = event.getEmployees();
    }
}
