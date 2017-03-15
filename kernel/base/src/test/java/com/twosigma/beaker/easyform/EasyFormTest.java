/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beaker.easyform;

import com.twosigma.beaker.KernelTest;
import com.twosigma.beaker.jupyter.KernelManager;
import com.twosigma.beaker.widgets.CommFunctionality;
import com.twosigma.beaker.widgets.box.Box;
import com.twosigma.beaker.widgets.strings.Text;
import com.twosigma.jupyter.message.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.twosigma.beaker.widgets.TestWidgetUtils.getData;
import static com.twosigma.beaker.widgets.TestWidgetUtils.getValueForProperty;
import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyDisplayMsg;
import static com.twosigma.beaker.widgets.TestWidgetUtils.verifyOpenCommMsg;
import static com.twosigma.beaker.widgets.Widget.DESCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

public class EasyFormTest {

  private KernelTest kernel;

  @Before
  public void setUp() throws Exception {
    kernel = new KernelTest();
    KernelManager.register(kernel);
  }

  @After
  public void tearDown() throws Exception {
    KernelManager.register(null);
  }

  @Test
  public void shouldCreateEasyFormWithTextField() throws Exception {
    //given
    String label = "text1";
    //when
    EasyForm easyForm = new EasyForm("EasyForm with text field");
    easyForm.addTextField(label, 10);
    DisplayEasyForm.display(easyForm);
    //then
    verifyTextField(kernel.getPublishedMessages().subList(0, 2));
    verifyTextFieldLabel(kernel.getPublishedMessages().get(2), label);
    verifyEasyForm(kernel.getPublishedMessages().subList(3, 5), easyForm.getCommFunctionalities());
    verifyDisplayMsg(kernel.getPublishedMessages().get(5));
  }

  private void verifyTextFieldLabel(Message message, String expectedLabel) {
    String label = getValueForProperty(message, DESCRIPTION, String.class);
    assertThat(label).isEqualTo(expectedLabel);
  }

  private void verifyEasyForm(List<Message> messages, List<CommFunctionality> children) {
    verifyOpenCommMsg(messages, EasyFormView.MODEL_NAME_VALUE, EasyFormView.VIEW_NAME_VALUE);
    verifyChildren(messages.get(1), children);
  }

  private void verifyTextField(List<Message> messages) {
    verifyOpenCommMsg(messages, Text.MODEL_NAME_VALUE, Text.VIEW_NAME_VALUE);
  }

  private void verifyChildren(Message message, List<CommFunctionality> children) {
    Map data = getData(message);
    Object[] objects = (Object[]) data.get(Box.CHILDREN);
    assertThat(objects.length).isEqualTo(children.size());
    for (int i = 0; i < children.size(); i++) {
      assertThat(Box.IPY_MODEL + children.get(i).getComm().getCommId()).isEqualTo(objects[i]);
    }
  }

}