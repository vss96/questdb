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

package io.questdb.cutlass.http;

import io.questdb.cairo.CairoException;

/**
 * Utility class for detecting memory-related exceptions in HTTP request handling.
 * This helps the HTTP server handle OOM conditions gracefully by sending proper
 * error responses instead of timing out.
 */
public class HttpOomErrorHandler {
    
    /**
     * Checks if the given exception is an OOM exception.
     * 
     * @param t the throwable to check
     * @return true if the exception is an OOM exception, false otherwise
     */
    public static boolean isOomException(Throwable t) {
        if (t instanceof CairoException) {
            CairoException ce = (CairoException) t;
            return ce.isOutOfMemory();
        }
        return false;
    }
    
    /**
     * Checks if the given exception is a memory-related exception.
     * This includes OOM exceptions and other memory limit exceptions.
     * 
     * @param t the throwable to check
     * @return true if the exception is memory-related, false otherwise
     */
    public static boolean isMemoryException(Throwable t) {
        if (isOomException(t)) {
            return true;
        }
        
        if (t instanceof CairoException) {
            CairoException ce = (CairoException) t;
            CharSequence message = ce.getFlyweightMessage();
            if (message != null) {
                String msg = message.toString().toLowerCase();
                return msg.contains("memory") || msg.contains("oom");
            }
        }
        
        return false;
    }
}

