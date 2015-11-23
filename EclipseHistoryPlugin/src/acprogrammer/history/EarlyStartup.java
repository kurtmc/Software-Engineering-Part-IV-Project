package acprogrammer.history;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * This class is based of the answer to this stackoverflow question
 * http://stackoverflow.com/questions/26871970/eclipse-plugin-development-how-to-listen-events-in-eclipse-editor
 */
public class EarlyStartup implements IStartup {
	
	private Logger logger;
	
	private long time = 0;
	
	private final int commitInterval = 1000;
	
	private ITextEditor lastEditor = null;
	
	private void doCommit() {
		// Only save max every 1s
        long delta = System.currentTimeMillis() - time;
    	if (delta > commitInterval) {
		
			// Get file path
    		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() != null)
    			lastEditor = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    		
    		if (lastEditor == null)
    			return;
		    
    		IEditorInput input1 = lastEditor.getEditorInput();
            IFile file = ((FileEditorInput) input1).getFile();
                        
            // Project path, this is where we should git init
            String projectPath = file.getParent().getProject().getLocation().toOSString();
          
            StoreHistory.gitInit(projectPath);
            
        
        
            lastEditor.doSave(null);
	        StoreHistory.commitFile(projectPath, file.getProjectRelativePath().toOSString());
        	time = System.currentTimeMillis();
    	}  
	}
	
	// Single document listener
	private final IDocumentListener documentListener = new IDocumentListener() {
		
		@Override
		public void documentChanged(DocumentEvent event) {
			doCommit();			          
		}
		
		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
			doCommit();
		}
	};

	
	private IWindowListener generateWindowListener() 
	{
	    return new IWindowListener() {
	        @Override
	        public void windowOpened(IWorkbenchWindow window) {
	        	logger.log("Window Opened");
	            IWorkbenchPage activePage = window.getActivePage(); 
	            activePage.addPartListener(generateIPartListener2());
	            doCommit();
	        }
	
	        @Override
	        public void windowDeactivated(IWorkbenchWindow window) {
	        	logger.log("Window Deactivated");
	        	IWorkbenchPage activePage = window.getActivePage(); 
	            activePage.addPartListener(generateIPartListener2());
	            doCommit();
	        }
	
	        @Override
	        public void windowClosed(IWorkbenchWindow window) {
	        	logger.log("Window Closed");
	        	IWorkbenchPage activePage = window.getActivePage(); 
	            activePage.addPartListener(generateIPartListener2());
	            doCommit();
	        }
	
	        @Override
	        public void windowActivated(IWorkbenchWindow window) {
	        	logger.log("Window Activated");
	        	IWorkbenchPage activePage = window.getActivePage(); 
	            activePage.addPartListener(generateIPartListener2());
	            doCommit();
	        }
	    };
	}
	
	private IPartListener2 generateIPartListener2() 
	{
	    return new IPartListener2() {
	
	        private void checkPart(IWorkbenchPartReference partRef) {
	        	IWorkbenchPart part = partRef.getPart(false);
	            if (part instanceof IEditorPart)
	            {
	                IEditorPart editor = (IEditorPart) part;
	                IEditorInput input = editor.getEditorInput();
	                if (editor instanceof ITextEditor && input instanceof FileEditorInput)  //double check.  Error Editors can also bring up this call
	                {
	                    IDocument document=(((ITextEditor)editor).getDocumentProvider()).getDocument(input);
	                
	                    
	                    // Make sure that only one document listener is attached
	                    document.removeDocumentListener(documentListener);
	                    document.addDocumentListener(documentListener);
	                }
	                doCommit();
	            }
	        }
	
	        @Override
	        public void partOpened(IWorkbenchPartReference partRef) {
	        	logger.log("Part Opened");
	            checkPart(partRef);
	        }
	
	        @Override
	        public void partInputChanged(IWorkbenchPartReference partRef) 
	        {
	        	logger.log("Part Input Changed");
	            checkPart(partRef);
	        }           
	
	        @Override
	        public void partVisible(IWorkbenchPartReference partRef){
	        	 checkPart(partRef);
	        }
	
	        @Override
	        public void partHidden(IWorkbenchPartReference partRef) {
	        	 checkPart(partRef);
	        }
	
	        @Override
	        public void partDeactivated(IWorkbenchPartReference partRef)  {
	        	 checkPart(partRef);
	        }
	
	        @Override
	        public void partClosed(IWorkbenchPartReference partRef) {
	        	 checkPart(partRef);
	        }
	
	        @Override
	        public void partBroughtToTop(IWorkbenchPartReference partRef) {
	        	 checkPart(partRef);
	        }
	
	        @Override
	        public void partActivated(IWorkbenchPartReference partRef) {
	        	 checkPart(partRef);
	        }
	    };
	}

	@Override
	public void earlyStartup() {
		logger = new Logger();
		logger.log("Listener attached to Workbench");

		IWorkbench wb = PlatformUI.getWorkbench();
	    wb.addWindowListener(generateWindowListener());
	}
}
