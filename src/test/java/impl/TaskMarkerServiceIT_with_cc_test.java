package impl;

import impl.mockedJNDI.JavaContextFactory;
import impl.mockedJNDI.SpringFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import se.cambio.nova.medication.service.MedicationScheduleDataService;
import se.cambio.nova.model.marker.TaskMarker;
import se.cambio.nova.model.marker.TaskMarkerIdFactory;
import se.cambio.nova.model.marker.dto.TaskMarkerDTO;
import se.cambio.nova.model.marker.dto.TaskMarkerEntityDTOIdFactory;
import se.cambio.nova.model.marker.dto.impl.TaskMarkerEntityDTOIdFactoryImpl;
import se.cambio.nova.model.marker.impl.TaskMarkerIdFactoryImpl;
import se.cambio.nova.model.marker.persistence.TaskMarkerDAO;
import se.cambio.nova.model.marker.service.TaskMarkerService;
import se.cambio.platform.cc.hcm.contact.dto.ContactEntityDTOIdFactory;
import se.cambio.test.itf.core.TestBase;
import se.cambio.test.itf.core.annotations.TestConfiguration;

import javax.naming.InitialContext;
import java.util.List;

@ContextConfiguration("/fromjboss/jboss_spring.xml")
@TestConfiguration(login = "Himali", userRole = "Doctor*", workingUnit = "The Medicine department*")
public class TaskMarkerServiceIT_with_cc_test extends TestBase
{
    static {
        System.setProperty("itf.db.host", "cllk-hasanthaal");
        System.setProperty("itf.db.port", "15788");
        System.setProperty("itf.db.name", "TeamCI_Stella_R82");
//        System.setProperty("itf.db.name", "LineCI_SUP_HF_R81_11NEW");
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


//        taskMarkerService.addComment(taskMarkerEntityDTOIdFactory.createId("2502"), "Nice :P  !!!");
//        System.out.println("----------");
//        TaskMarker taskMarker = taskMarkerDAO.load(taskMarkerIdFactory.createId("2502"));
//        System.out.println("Name : " + taskMarker.getName() + " - Comment : " + taskMarker.getComment());
    }
}
