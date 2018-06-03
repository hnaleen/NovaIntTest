package impl;

import impl.mockedJNDI.JavaContextFactory;
import impl.newJndi.DatabaseContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import se.cambio.nova.model.marker.TaskMarker;
import se.cambio.nova.model.marker.TaskMarkerIdFactory;
import se.cambio.nova.model.marker.dto.TaskMarkerDTO;
import se.cambio.nova.model.marker.dto.TaskMarkerEntityDTOIdFactory;
import se.cambio.nova.model.marker.persistence.TaskMarkerDAO;
import se.cambio.nova.model.marker.service.TaskMarkerService;
import se.cambio.platform.cc.hcm.contact.dto.ContactEntityDTOIdFactory;
import se.cambio.test.itf.core.annotations.TestConfiguration;

import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import java.util.List;

@ContextConfiguration("/fromjboss/jboss_spring.xml")
@TestConfiguration(login = "Himali", userRole = "Doctor*", workingUnit = "The Medicine department*")
public class TaskMarkerServiceIT extends AbstractTransactionalTestNGSpringContextTests
{
    static {
//        JavaContextFactory.getInstance().activate();
        try {
            NamingManager.setInitialContextFactoryBuilder(new DatabaseContextFactory());
        } catch (NamingException e) {
            e.printStackTrace();
        }


        System.setProperty("itf.db.host", "cllk-hasanthaal");
        System.setProperty("itf.db.port", "1433");
        System.setProperty("itf.db.name", "LineCI_SUP_HF_R81_11NEW");
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
        taskMarkerService.addComment(taskMarkerEntityDTOIdFactory.createId("2502"), "Nice :P  !!!");
        System.out.println("----------");
        TaskMarker taskMarker = taskMarkerDAO.load(taskMarkerIdFactory.createId("2502"));
        System.out.println("Name : " + taskMarker.getName() + " - Comment : " + taskMarker.getComment());
    }
}
