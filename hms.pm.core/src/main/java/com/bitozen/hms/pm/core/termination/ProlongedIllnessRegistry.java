package com.bitozen.hms.pm.core.termination;

import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import com.bitozen.hms.pm.command.termination.ProlongedIllnessRegistryCreateCommand;
import com.bitozen.hms.pm.command.termination.ProlongedIllnessRegistryChangeCommand;
import com.bitozen.hms.pm.command.termination.ProlongedIllnessRegistryDeleteCommand;
import com.bitozen.hms.pm.event.termination.ProlongedIllnessRegistryCreateEvent;
import com.bitozen.hms.pm.event.termination.ProlongedIllnessRegistryChangeEvent;
import com.bitozen.hms.pm.event.termination.ProlongedIllnessRegistryDeleteEvent;
import java.util.Date;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

/**
 *
 * @author Dumayangsari
 */
@Aggregate
public class ProlongedIllnessRegistry {
    
    @AggregateIdentifier
    private String piID;
    private Date startDate;
    private Date endDate;
    private String docURL;
    private String employee;
    private String requestor;
    private String ilnessType;
    private ProlongedIllnessStatus piStatus;
    private String reason;
    private String metadata;
    private String token;
    
    
    private String recordID;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    
    public ProlongedIllnessRegistry(){
        
    }
    
    @CommandHandler
    public ProlongedIllnessRegistry(ProlongedIllnessRegistryCreateCommand command){
        AggregateLifecycle.apply(new ProlongedIllnessRegistryCreateEvent(
                command.getRecordID(),
                command.getCreatedBy(),
                command.getCreatedDate(),
                command.getPiID(),
                command.getStartDate(),
                command.getEndDate(),
                command.getDocURL(),
                command.getEmployee(),
                command.getRequestor(),
                command.getIlnessType(),
                command.getPiStatus(),
                command.getReason(),
                command.getMetadata(),
                command.getToken()
        ));
    }
    
    @CommandHandler
    public void handle(ProlongedIllnessRegistryChangeCommand command){
        AggregateLifecycle.apply(new ProlongedIllnessRegistryChangeEvent(
                command.getUpdatedBy(),
                command.getUpdatedDate(),
                command.getPiID(),
                command.getStartDate(),
                command.getEndDate(),
                command.getDocURL(),
                command.getEmployee(),
                command.getRequestor(),
                command.getIlnessType(),
                command.getPiStatus(),
                command.getReason(),
                command.getMetadata(),
                command.getToken()
        ));
    }
    
    @CommandHandler
    public void handle(ProlongedIllnessRegistryDeleteCommand command){
        AggregateLifecycle.apply(new ProlongedIllnessRegistryDeleteEvent(
                command.getPiID(),
                command.getUpdatedBy()
        ));
    }
    
    @EventSourcingHandler
    public void on(ProlongedIllnessRegistryCreateEvent event){
        this.piID = event.getPiID();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.docURL = event.getDocURL();
        this.employee = event.getEmployee();
        this.requestor = event.getRequestor();
        this.ilnessType = event.getIlnessType();
        this.piStatus = event.getPiStatus();
        this.reason = event.getReason();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.recordID = event.getRecordID();
        this.createdBy = event.getCreatedBy();
        this.createdDate = event.getCreatedDate();       
    }
    
    @EventSourcingHandler
    public void on(ProlongedIllnessRegistryChangeEvent event){
        this.piID = event.getPiID();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.docURL = event.getDocURL();
        this.employee = event.getEmployee();
        this.requestor = event.getRequestor();
        this.ilnessType = event.getIlnessType();
        this.piStatus = event.getPiStatus();
        this.reason = event.getReason();
        this.metadata = event.getMetadata();
        this.token = event.getToken();
        this.updatedBy = event.getUpdatedBy();
        this.updatedDate = event.getUpdatedDate();      
    }
    
    @EventSourcingHandler
    public void on(ProlongedIllnessRegistryDeleteEvent event){
        this.piID = event.getPiID();
        this.updatedBy = event.getUpdatedBy();      
    }
}
