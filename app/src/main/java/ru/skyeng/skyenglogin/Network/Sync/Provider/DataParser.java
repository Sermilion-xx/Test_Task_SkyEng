/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.skyeng.skyenglogin.network.sync.provider;

import java.io.InputStream;
import java.util.List;

/*
Класс который парсит принятые данные
 */
public class DataParser {

    public String title;

    public List<Entry> parse(InputStream stream){
        return null;
    }
    public class Entry{
        public String id = null;
        public String title = null;
        public long published = 0;
    }
}
