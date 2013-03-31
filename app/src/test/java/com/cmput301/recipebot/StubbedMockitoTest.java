

/*
 * Copyright 2013 Adam Saturna
 *  Copyright 2013 Brian Trinh
 *  Copyright 2013 Ethan Mykytiuk
 *  Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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