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
package net.sourceforge.plantuml.ugraphic.eps;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public class DriverEllipseEps implements UDriver<EpsGraphics> {

	private final ClipContainer clipContainer;

	public DriverEllipseEps(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, EpsGraphics eps) {
		final UEllipse shape = (UEllipse) ushape;
		final double width = shape.getWidth();
		final double height = shape.getHeight();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			if (clip.isInside(x, y) == false) {
				return;
			}
			if (clip.isInside(x + width, y + height) == false) {
				return;
			}
		}

		// Shadow
		if (shape.getDeltaShadow() != 0) {
			eps.epsEllipseShadow(x + width / 2, y + height / 2, width / 2, height / 2, shape.getDeltaShadow());
		}

		eps.setFillColor(mapper.toColor(param.getBackcolor()));
		eps.setStrokeColor(mapper.toColor(param.getColor()));
		eps.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDashVisible(), param.getStroke()
				.getDashSpace());

		if (shape.getStart() == 0 && shape.getExtend() == 0) {
			eps.epsEllipse(x + width / 2, y + height / 2, width / 2, height / 2);
		} else {
			eps.epsEllipse(x + width / 2, y + height / 2, width / 2, height / 2, shape.getStart(), shape.getExtend());
		}
	}

}