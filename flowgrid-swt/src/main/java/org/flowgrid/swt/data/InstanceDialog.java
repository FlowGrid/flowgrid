package org.flowgrid.swt.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.flowgrid.model.Callback;
import org.flowgrid.model.Classifier;
import org.flowgrid.model.Instance;
import org.flowgrid.model.Property;
import org.flowgrid.model.container.Grid;
import org.flowgrid.swt.SwtFlowgrid;
import org.flowgrid.swt.dialog.AlertDialog;
import org.flowgrid.swt.dialog.DialogInterface;

public class InstanceDialog {
    AlertDialog alert;

    public InstanceDialog(SwtFlowgrid flowgrid, String title, final Classifier classifier, final Instance originalInstance, final Callback<Instance> callback) {
        alert = new AlertDialog(flowgrid.shell());
        alert.setTitle(title == null || title.isEmpty() ? "Edit " + classifier.name() : title);

        ScrolledComposite scrolledComposite = flowgrid.createVerticalScrolledComposite(alert.getContentContainer());
        GridData listData = new GridData(SWT.FILL, SWT.FILL, true, true);
        listData.heightHint = flowgrid.getMinimumListHeight();
        scrolledComposite.setLayoutData(listData);

        Composite propertyPanel = new Composite(scrolledComposite, SWT.NONE);
        GridLayout propertyLayout = new GridLayout(2, false);
        propertyLayout.marginHeight = 0;
        propertyLayout.marginWidth = 0;
        propertyPanel.setLayout(propertyLayout);
        scrolledComposite.setContent(propertyPanel);

        final Instance instance = originalInstance == null ? new Instance(classifier) : originalInstance;

        for (Property property: classifier.properties(null)) {
            new Label(propertyPanel, SWT.NONE).setText(property.name());
            PropertyComponent propertyComponent = new PropertyComponent(propertyPanel, flowgrid, property, instance, false);
            propertyComponent.getControl().setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        }

        if (originalInstance == null) {
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callback.cancel();
                }
            });
        }

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.run(instance);
            }
        });


    }

    public void show() {
        alert.show();
    }
}
