package com.bitozen.hms.pm.common.dto.command.termination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TerminationDocumentCreateDTO implements Serializable{
    
    private String docID;
    private String docName;
    private String docType;
    private String docURL;
    
    @JsonIgnore
    public TerminationDocumentCreateDTO getInstance(){
        return new TerminationDocumentCreateDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "doc name",
                "KEY001",
                "URL"
        
        );
    
    }
    
}
