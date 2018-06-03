package impl.newJndi;

import impl.MockFactory;
import org.apache.commons.dbcp.BasicDataSource;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.naming.*;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Enumeration;
import java.util.Properties;

public class DatabaseContext extends InitialContext
{
    Properties prop = new Properties();

    DatabaseContext() throws NamingException
    {
        prop.put("java:jboss/datasources/ix3", getDataSource());
        prop.put("java:jboss/TransactionManager", getTransactionManager());
        prop.put("java:jboss/UserTransaction", getUserTransaction());
        prop.put("java:jboss/infinispan/container/hibernate", getInfinispanCacheContainer());
    }

    @Override
    public Object lookup(String name) throws NamingException
    {
        try {
            //our connection strings


            Object value = prop.get(name);
            if (value ==null)
            {
                System.out.println("++++++++++++++ Following lookup not found : " + name);
            }
//            return (value != null) ? value : super.lookup(name);
            return value;
        }
        catch(Exception e) {
            System.err.println("Lookup Problem " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object lookup(Name name) throws NamingException
    {
        CustomName customName = (CustomName) name;
        return prop.get(customName.name);
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException
    {
        return new CustomNameParse();
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException
    {
        return new CustomNameParse();
    }

    public Object getInfinispanCacheContainer()
    {
        return MockFactory.mock(EmbeddedCacheManager.class);
    }

    class CustomNameParse implements NameParser
    {

        @Override
        public Name parse(final String name) throws NamingException
        {
            return new CustomName(name);
        }
    }

    class CustomName implements Name
    {
        private final String name;

        public CustomName(String name)
        {
            this.name = name;
        }

        @Override
        public int compareTo(Object obj)
        {
            CustomName other = (CustomName) obj;
            return this.name.compareTo(other.name);
        }

        @Override
        public int size()
        {
            return name.length();
        }

        @Override
        public boolean isEmpty()
        {
            return name.isEmpty();
        }

        @Override
        public Enumeration<String> getAll()
        {
            return null;
        }

        @Override
        public String get(int posn)
        {
            return name;
        }

        @Override
        public Name getPrefix(int posn)
        {
            return null;
        }

        @Override
        public Name getSuffix(int posn)
        {
            return this;
        }

        @Override
        public boolean startsWith(Name n)
        {
            return true;
        }

        @Override
        public boolean endsWith(Name n)
        {
            return true;
        }

        @Override
        public Name addAll(Name suffix) throws InvalidNameException
        {
            return this;
        }

        @Override
        public Name addAll(int posn, Name n) throws InvalidNameException
        {
            return this;
        }

        @Override
        public Name add(String comp) throws InvalidNameException
        {
            return this;
        }

        @Override
        public Name add(int posn, String comp) throws InvalidNameException
        {
            return this;
        }

        @Override
        public Object remove(int posn) throws InvalidNameException
        {
            return null;
        }

        @Override
        public Object clone()
        {
            return this;
        }

        @Override
        public boolean equals(Object obj)
        {
            boolean equals = false;
            if (obj instanceof CustomName)
            {
                CustomName other = (CustomName) obj;
                equals = name.equalsIgnoreCase(other.name);
            }
            return equals;
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
