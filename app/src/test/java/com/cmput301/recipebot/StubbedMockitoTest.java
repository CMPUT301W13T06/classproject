

package com.cmput301.recipebot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static org.mockito.Mockito.verify;

/**
 * Stubbed mockito Tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class StubbedMockitoTest {


    @Mock
    private OutputStream outputStream;

    /**
     * Set up default mocks
     *
     * @throws IOException
     */
    @Before
    public void before() throws IOException {

    }

    /**
     * Stubbed Test
     *
     * @throws IOException
     */
    @Test
    public void getStubbedTest() throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        outputStreamWriter.close();
        verify(outputStream).close();
    }
}