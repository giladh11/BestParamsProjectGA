/*******************************************************************************
 * Copyright 2012 Yuriy Lagodiuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package evolutionGaTools;

/**
 * only used for printing
 * @param <C>
 * @param <T>
 */
public interface IterartionListenerHigh<C extends Chromosome<C, T>, T extends Comparable<T>> {

    void update(GeneticAlgorithmHigherLevel<C, T> environment);
    
}