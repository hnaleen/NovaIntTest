package impl;

import impl.mockedJNDI.JavaContextFactory;
import impl.newJndi.DatabaseContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import se.cambio.nova.model.marker.TaskMarker;
import se.cambio.nova.model.marker.TaskMarkerIdFactory;
import se.cambio.nova.model.marker.dto.TaskMarkerDTO;
import se.cambio.nova.model.marker.dto.TaskMarkerEntityDTOIdFactory;
import se.cambio.nova.model.marker.persistence.TaskMarkerDAO;
import se.cambio.nova.model.marker.service.TaskMarkerService;
import se.cambio.platform.cc.hcm.contact.dto.ContactEntityDTOIdFactory;
import se.cambio.test.itf.core.TestBase;
import se.cambio.test.itf.core.annotations.TestConfiguration;

import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import java.util.List;

//@ContextConfiguration(locations = {
//        "classpath*:META-INF/se.cambio.platform.sdk.ejb.server/ejb-context.xml",
//        "classpath*:META-INF/se.cambio.platform.sdk/spring-context-market-adaptations.xml"})

@ContextConfiguration("/se/cambio/nova/model/marker/service/impl/TaskMarkerService_working.xml")
@TestConfiguration(login = "Himali", userRole = "Doctor*", workingUnit = "The Medicine department*")
public class TaskMarkerServiceIT_with_cc_testIT extends TestBase
{
    static {
//        System.setProperty("itf.db.host", "cllk-hasanthaal");
//        System.setProperty("itf.db.port", "15788");
//        System.setProperty("itf.db.name", "TeamCI_Stella_R82");
    }

    @Autowired
    private TaskMarkerService taskMarkerService;

    @Autowired
    private ContactEntityDTOIdFactory contactEntityDTOIdFactory;

    @Autowired
    private TaskMarkerEntityDTOIdFactory taskMarkerEntityDTOIdFactory;

    @Autowired
    TaskMarkerIdFactory taskMarkerIdFactory;

    @Autowired
    TaskMarkerDAO taskMarkerDAO;

    @Test
    public void testFindByContact()
    {
        List byContact = taskMarkerService.findByContact(contactEntityDTOIdFactory.createId("23333"));
        for (Object markerObj: byContact)
        {
            TaskMarkerDTO taskMarker = (TaskMarkerDTO) markerObj;
            System.out.println(taskMarker + " name:" + taskMarker.getName() + " comment:" + taskMarker.getComment());
        }
        System.out.println("No of TaskMarkers : " + byContact.size());
    }

//    @Commit
    @Test
    public void testAddComment()
    {
        taskMarkerService.addComment(taskMarkerEntityDTOIdFactory.createId("1006"), "Nice :P  !!!");
        System.out.println("----------");
        TaskMarker taskMarker = taskMarkerDAO.load(taskMarkerIdFactory.createId("1006"));
        System.out.println("Name : " + taskMarker.getName() + " - Comment : " + taskMarker.getComment());
    }
}
