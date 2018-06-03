package impl.newJndi;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import java.util.Hashtable;

public class DatabaseContextFactory implements InitialContextFactory, InitialContextFactoryBuilder
{
    DatabaseContext contxt;

    public DatabaseContextFactory()
    {
        try {
            this.contxt = new DatabaseContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public Context getInitialContext(Hashtable<?, ?> environment)
            throws NamingException
    {
//        return new DatabaseContext();
        return contxt;
    }

    public InitialContextFactory createInitialContextFactory(
            Hashtable<?, ?> environment) throws NamingException
    {
//        return new DatabaseContextFactory();
        return this;
    }

}
