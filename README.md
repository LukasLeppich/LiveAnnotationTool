#Live Annotation Tool
<sub>*(For the machine learning course at the University of Würzburg)*</sub>

**This tool is still under development, please make copies of your annotation files before use!**

This tool should provide an easy way to create annotation files for gesture learning.

To synchronize the annotation times, you have to output the current timestamp in milliseconds at the start of recording.
The annotation tool also creates an file with the current timestamp.
After recording you can use the review annotation view to update the annotation times.


## Workflows
### Recording
1. Select "Create annotations" on the main view
2. Select output file
3. Add annotation labels
4. Press "Start"
5. Start Simulator X recording
6. Press ENTER to start annotation recording
7. Switch annotation with number keys
8. Press and hold the mouse key or any keyboard key to start an annotation
9. Release the pressed key to end the annotation
10. Press ESC to stop recording

### Review annotation file

1. Select "Review annotations" on the main view
2. Select the annotation file
3. Press "Load" to load the annotation file
4. Enter the timestamp of the recording start into the "Recording started" field
5. Press "Update times"

### Create EAF-File for ELAN

1. Select "Review annotation" on the main view
2. Select the annotation File
3. Select the video file of the recording
4. Specify the EAF output filename
5. Press "Create EAF-File"


### Rename recording files

Select reference file and rename selected files to match filename while keeping extensions.

1. Select "Review annotation" on the main view
2. Select annotation file
3. Select "Name reference" file and click "Load files"
4. Select files to rename
5. Press "Rename selected Files"
