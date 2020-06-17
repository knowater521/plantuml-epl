/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Notes extends AbstractEvent implements Event, Iterable<Note> {

	private final List<Note> notes = new ArrayList<Note>();

	public Notes(Note n1, Note n2) {
		notes.add(n1);
		notes.add(n2);
	}

	public void add(Note n) {
		notes.add(n);
	}

	public boolean dealWith(Participant someone) {
		for (Note n : notes) {
			if (n.dealWith(someone)) {
				return true;
			}
		}
		return false;
	}

	public Iterator<Note> iterator() {
		return notes.iterator();
	}

	public Note get(int i) {
		return notes.get(i);
	}

	public List<Note> asList() {
		return Collections.unmodifiableList(notes);
	}
}