/*
 * Copyright (c) 2009, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.qi4j.bootstrap;

/**
 * Visitor interface to visit the whole or parts of an assembly. Implement this
 * interface and call visit() on ApplicationAssembly, LayerAssembly or ModuleAssembly.
 * <p/>
 * This can be used to, for example, add metadata to all entities, add concerns on composites, or similar.
 */
public interface AssemblyVisitor
{
    public void visitApplication(ApplicationAssembly assembly);
    public void visitLayer(LayerAssembly assembly);
    public void visitModule(ModuleAssembly assembly);

    public void visitComposite(CompositeDeclaration declaration);
    public void visitEntity(EntityDeclaration declaration);
    public void visitService(ServiceDeclaration declaration);
    public void visitImportedService(ImportedServiceDeclaration declaration);
    public void visitValue(ValueDeclaration declaration);
    public void visitObject(ObjectDeclaration declaration);
}
