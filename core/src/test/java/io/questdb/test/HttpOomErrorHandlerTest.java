/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2024 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.test;

import io.questdb.cairo.CairoException;
import io.questdb.cutlass.http.HttpOomErrorHandler;
import org.junit.Assert;
import org.junit.Test;

public class HttpOomErrorHandlerTest {

    @Test
    public void testIsOomException() {
        // Test OOM CairoException
        CairoException oomException = CairoException.nonCritical().setOutOfMemory(true);
        Assert.assertTrue("Should detect OOM exception", HttpOomErrorHandler.isOomException(oomException));

        // Test non-OOM CairoException
        CairoException normalException = CairoException.nonCritical();
        Assert.assertFalse("Should not detect non-OOM exception", HttpOomErrorHandler.isOomException(normalException));

        // Test non-CairoException
        RuntimeException runtimeException = new RuntimeException("test");
        Assert.assertFalse("Should not detect non-CairoException", HttpOomErrorHandler.isOomException(runtimeException));
    }

    @Test
    public void testIsMemoryException() {
        // Test OOM CairoException
        CairoException oomException = CairoException.nonCritical().setOutOfMemory(true);
        Assert.assertTrue("Should detect OOM as memory exception", HttpOomErrorHandler.isMemoryException(oomException));

        // Test memory limit exceeded exception
        CairoException memoryLimitException = CairoException.nonCritical().put("memory limit exceeded");
        Assert.assertTrue("Should detect memory limit exception", HttpOomErrorHandler.isMemoryException(memoryLimitException));

        // Test OOM in message
        CairoException oomMessageException = CairoException.nonCritical().put("OOM error occurred");
        Assert.assertTrue("Should detect OOM in message", HttpOomErrorHandler.isMemoryException(oomMessageException));

        // Test normal CairoException
        CairoException normalException = CairoException.nonCritical().put("normal error");
        Assert.assertFalse("Should not detect normal exception as memory exception", HttpOomErrorHandler.isMemoryException(normalException));

        // Test non-CairoException
        RuntimeException runtimeException = new RuntimeException("test");
        Assert.assertFalse("Should not detect non-CairoException as memory exception", HttpOomErrorHandler.isMemoryException(runtimeException));
    }

    @Test
    public void testIsMemoryExceptionWithNullMessage() {
        // Test CairoException with null message
        CairoException exceptionWithNullMessage = CairoException.nonCritical();
        Assert.assertFalse("Should not detect exception with null message as memory exception", 
                          HttpOomErrorHandler.isMemoryException(exceptionWithNullMessage));
    }
}

