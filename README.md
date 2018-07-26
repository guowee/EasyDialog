# EasyDialog
Android自定义Dialog

## Dependencies
```
compile 'com.muse.easy.dialog:easy-dialog:1.0.1'
```

## Usage
```
  EasyDialog.Builder builder = new EasyDialog.Builder(mContext);
  builder.title(title).content(message).positiveText("OK").negativeText("CANCEL");
  builder.create().show();
```				
### Screenshot
![image][./screenshot/dialog.png]