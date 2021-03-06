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
package net.sourceforge.plantuml.hector;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.geom.LineSegmentDouble;

public class Box2D {

	final private double x1;
	final private double y1;
	final private double x2;
	final private double y2;

	private Box2D(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public static Box2D create(double x, double y, Dimension2D dim) {
		return new Box2D(x, y, x + dim.getWidth(), y + dim.getHeight());
	}

	@Override
	public String toString() {
		return "Box [" + x1 + "," + y1 + "] [" + x2 + "," + y2 + "]";
	}

	public boolean doesIntersect(LineSegmentDouble seg) {
		if (seg.doesIntersect(new LineSegmentDouble(x1, y1, x2, y1))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x2, y1, x2, y2))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x2, y2, x1, y2))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x1, y2, x1, y1))) {
			return true;
		}
		return false;
	}

}
