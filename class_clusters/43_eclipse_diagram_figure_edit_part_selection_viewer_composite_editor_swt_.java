package org.dbflute.erflute.editor.controller.command.diagram_contents.element.node.group;

import java.util.List;

import org.dbflute.erflute.editor.controller.command.AbstractCommand;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.ermodel.ERVirtualDiagram;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.ermodel.WalkerGroup;

/**
 * @author modified by jflute (originated in ermaster)
 */
public class ChangeVirtualWalkerGroupCommand extends AbstractCommand {

    private final ERVirtualDiagram vdiagram;
    private final List<WalkerGroup> oldGroups;
    private final List<WalkerGroup> walkerGroups;

    public ChangeVirtualWalkerGroupCommand(ERVirtualDiagram vdiagram, List<WalkerGroup> walkerGroups) {
        this.vdiagram = vdiagram;
        this.oldGroups = vdiagram.getWalkerGroups();
        this.walkerGroups = walkerGroups;
    }

    @Override
    protected void doExecute() {
        vdiagram.setWalkerGroups(walkerGroups);
    }

    @Override
    protected void doUndo() {
        vdiagram.setWalkerGroups(oldGroups);
    }
}

--------------------

package AllocatorMetamodel.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;

/**
 * @generated
 */
public class HWNodeHWResourceCompartmentEditPart extends
		ListCompartmentEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 7004;

	/**
	 * @generated
	 */
	public HWNodeHWResourceCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected boolean hasModelChildrenChanged(Notification evt) {
		return false;
	}

	/**
	 * @generated
	 */
	public String getCompartmentName() {
		return AllocatorMetamodel.diagram.part.Messages.HWNodeHWResourceCompartmentEditPart_title;
	}

	/**
	 * @generated
	 */
	public IFigure createFigure() {
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) super
				.createFigure();
		result.setTitleVisibility(false);
		return result;
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
				EditPolicyRoles.SEMANTIC_ROLE,
				new AllocatorMetamodel.diagram.edit.policies.HWNodeHWResourceCompartmentItemSemanticEditPolicy());
		installEditPolicy(
				EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicyWithCustomReparent(
						AllocatorMetamodel.diagram.part.Allocator_metamodelVisualIDRegistry.TYPED_INSTANCE));
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(
				EditPolicyRoles.CANONICAL_ROLE,
				new AllocatorMetamodel.diagram.edit.policies.HWNodeHWResourceCompartmentCanonicalEditPolicy());
	}

	/**
	 * @generated
	 */
	protected void setRatio(Double ratio) {
		// nothing to do -- parent layout does not accept Double constraints as ratio
		// super.setRatio(ratio); 
	}

}

--------------------

package com.architexa.diagrams.relo.jdt.services;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;

import com.architexa.diagrams.jdt.Activator;
import com.architexa.diagrams.jdt.model.CodeUnit;
import com.architexa.diagrams.model.Artifact;
import com.architexa.diagrams.relo.jdt.ReloJDTPlugin;
import com.architexa.diagrams.relo.parts.MoreItemsEditPart;
import com.architexa.diagrams.services.PluggableTypes;
import com.architexa.diagrams.services.PluggableTypes.ImageDescriptorProvider;
import com.architexa.diagrams.services.PluggableTypes.PluggableTypeInfo;
import com.architexa.store.ReloRdfRepository;

public class PluggableEditPartSupport {
	static final Logger logger = ReloJDTPlugin.getLogger(PluggableEditPartSupport.class);
	
	public static class DefaultImageDescriptorProvider implements ImageDescriptorProvider {

		public ImageDescriptor getImageDescriptor(Artifact art, Resource typeRes, ReloRdfRepository repo) {
			ImageDescriptor desc = CodeUnit.getIconDescriptor(repo, art, typeRes);
			
			if (desc == null) {
		    	// see if there is another EP to render it
		        MoreItemsEditPart cuepInst = (MoreItemsEditPart) PluggableTypes.getController(repo, art.elementRes, typeRes);
		        if (cuepInst != null) return cuepInst.getIconDescriptor(art, typeRes);
		    	
			    logger.error("IconDesc requested for unknown type: " + typeRes, new Exception());
			    desc = CodeUnit.getImageDescriptorFromKey(ISharedImages.IMG_FIELD_PRIVATE);
			}
			return desc;
		}
		
	}
	public static DefaultImageDescriptorProvider defaultCodeUnitIconProvider = new DefaultImageDescriptorProvider();
	
	
	public static ImageDescriptor getIconDescriptor(ReloRdfRepository repo, Artifact art, Resource typeRes) {
		if("<clinit>".equals(art.queryName(repo))) {
			// Wrap in a JavaElementImageDescriptor since it sets the
			// size, which will prevent ugly stretching of images in menus
			ImageDescriptor desc = Activator.getImageDescriptor("icons/static_initializer.png");
			return new JavaElementImageDescriptor(desc, 0, JavaElementImageProvider.BIG_SIZE);
		}

		ImageDescriptor pluggableIcon = guessFromPluggableTypes(art, typeRes, repo);
		
		// check for icons for items without a pluggable type
		if (pluggableIcon == null)
			pluggableIcon = defaultCodeUnitIconProvider.getImageDescriptor(art, typeRes, repo);
		
		return pluggableIcon;
			
	}
	
	private static ImageDescriptor guessFromPluggableTypes(Artifact art, Resource typeRes, ReloRdfRepository repo) {
		PluggableTypeInfo pti = PluggableTypes.getRegisteredTypeInfo(art.elementRes, repo, (URI) typeRes);
		if (pti != null)
			return pti.iconProvider.getImageDescriptor(art, typeRes, repo);
		else
			return null;
	}

}

--------------------

