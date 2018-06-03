package impl;

import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ThrowsException;

public class MockFactory
{
    public static<T> T mock(Class<T> clazz)
    {
        MockSettings mockSettings = Mockito.withSettings();
        mockSettings.verboseLogging();
        T mock = Mockito.mock(clazz, mockSettings);
//        T mock = Mockito.mock(clazz, new ThrowsException(new UnmockedMethodCalledException()));
        return mock;

    }
}

class UnmockedMethodCalledException extends RuntimeException
{
    public UnmockedMethodCalledException()
    {
        super("An un-mocked method called on the mock object");
    }
}
