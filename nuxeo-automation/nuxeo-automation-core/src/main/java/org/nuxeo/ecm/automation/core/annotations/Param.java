/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.ecm.automation.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuxeo.ecm.automation.OperationParameters;

/**
 * To be used on an operation field to inject operation parameters from the current context.
 * If the parameter to inject cannot be found in the operation parameters map (or it is set to null)
 * then if required is true then an error is thrown otherwise the injection will not be done
 * (and any default value set in the code will be preserved).
 * The default is true - i.e. do not allow missing entries in operation parameter map.
 *   
 * @see OperationParameters 
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Param {

    /**
     * The parameter key in the operation parameters map. 
     * @return
     */
    String name();
    
    /**
     * If the parameter to inject cannot be found in the operation parameters map (or it is set to null)
     * then if required is true then an error is thrown otherwise the injection will not be done
     * (and any default value set in the code will be preserved).
     * The default is true - i.e. do not allow missing entries in operation parameter map.  
     * @return
     */
    boolean required() default true;
    
    /**
     * Optional attribute - useful to generate operation documentation.
     *
     * Provide a widget type to be used by the UI to edit this parameter.
     * If no widget is provided the default mechanism is to choose a widget compatible with the parameter type.
     * For example if the parameter has the type String the default is to use a TextBox but you can override this
     * by specifying a custom widget type like ListBox, TextArea etc. 
     *  
     * @return
     */
    String widget() default "";
    
    /**
     * Optional attribute - useful to generate operation documentation.
     *
     * Provide default values for the parameter widget.
     * If the parameter is rendered using a ListBox or ComboBox then this attribute can be used to hold the 
     * choices available in the list.
     * If the widget is not a list then this can be used to specify the default value for the widget. 
     */    
    String[] values() default {};
}
