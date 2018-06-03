package impl.mockedJNDI;

import impl.MockFactory;
import org.apache.commons.dbcp.BasicDataSource;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public class SpringFactory
{
    private static SpringFactory instance = new SpringFactory();

    public static SpringFactory getInstance()
    {
        return instance;
    }

    public void initializeJndi()
    {
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind("java:jboss/datasources/ix3", getDataSource());
        builder.bind("java:jboss/TransactionManager", getTransactionManager());
        builder.bind("java:jboss/UserTransaction", getUserTransaction());
        try {
            builder.activate();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private DataSource getDataSource() //TODO This is not set as autowired candidate
    {
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = new TransactionAwareDataSourceProxy();
        transactionAwareDataSourceProxy.setReobtainTransactionalConnections(true);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
        dataSource.setUrl("jdbc:jtds:sqlserver://cllk-hasanthaal:1433/LineCI_SUP_HF_R81_11NEW;prepareSQL=2\\");
        dataSource.setUsername("spider3");
        dataSource.setPassword("spider3");
        dataSource.setAccessToUnderlyingConnectionAllowed(true);

        transactionAwareDataSourceProxy.setTargetDataSource(dataSource);
        return transactionAwareDataSourceProxy;
    }


    private TransactionManager getTransactionManager()
    {
        return MockFactory.mock(TransactionManager.class);
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setDataSource(getDataSource());
//        return transactionManager;
    }

    private UserTransaction getUserTransaction()
    {
        return MockFactory.mock(UserTransaction.class);
    }
}
