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
package net.sourceforge.plantuml.hector2.layering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.hector2.continuity.Skeleton;

public class LayerFactory {

	public List<Layer> getLayers(Skeleton skeleton) {
		skeleton = skeleton.removeCycle();
		skeleton.computeLayers();
		final List<Layer> result = new ArrayList<Layer>();
		for (IEntity ent : skeleton.entities()) {
			ensureLayer(result, ent.getHectorLayer());
		}
		for (IEntity ent : skeleton.entities()) {
			final int layer = ent.getHectorLayer();
			result.get(layer).add(ent);
		}
		return Collections.unmodifiableList(result);
	}

	private void ensureLayer(List<Layer> result, int layerToAdd) {
		while (result.size() <= layerToAdd) {
			result.add(new Layer(result.size()));
		}

	}
}
