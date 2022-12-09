package br.com.brasizza.sunmi_k2_printer_plus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import com.sunmi.extprinterservice.*;

import java.util.Arrays;


public class SunmiPrinterMethod {

    private final String TAG = SunmiPrinterMethod.class.getSimpleName();
    private ArrayList<Boolean> _printingText = new ArrayList<Boolean>();
    private ExtPrinterService _printingService;
    private Context _context;

    public SunmiPrinterMethod(Context context) {
        this._context = context;
    }

    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                _printingService = ExtPrinterService.Stub.asInterface(service);
                // String serviceVersion = _printingService.getServiceVersion();
                int status = _printingService.getPrinterStatus();
                Toast
                        .makeText(
                                _context,
                                "Sunmi Printer Service Connected. Status: " + String.valueOf(status),
                                Toast.LENGTH_LONG
                        )
                        .show();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

                Toast
                        .makeText(
                                _context,
                                "Sunmi Printer Service Not Found",
                                Toast.LENGTH_LONG
                        ).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast
                    .makeText(
                            _context,
                            "Sunmi Printer Service Disconnected",
                            Toast.LENGTH_LONG
                    )
                    .show();
        }
    };

    public void bindPrinterService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.extprinterservice");
        intent.setAction("com.sunmi.extprinterservice.ExtPrinterService");
        _context.bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void unbindPrinterService() {
        _context.unbindService(connService);
    }

    public void initPrinter() {
        try {
            _printingService.printerInit();
        } catch (RemoteException e) {
        } catch (NullPointerException e) {

        }
    }

    public int updatePrinter() {
        try {
            final int status = _printingService.getPrinterStatus();
            Log.i("PRINTER", String.valueOf(status));
            return status;
        } catch (RemoteException e) {
            Log.i("Printer", "exception", e);
            return 0; // error
        } catch (NullPointerException e) {
            Log.i("Printer", "exception", e);
            return 0;
        }
    }

    public void printText(String text) {
        this._printingText.add(this._printText(text));
    }

    private Boolean _printText(String text) {
        try {
            _printingService.printText(text);
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public Boolean setAlignment(Integer alignment) {
        try {
            _printingService.setAlignMode(alignment);
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public Boolean setFontSize(int fontSize) {
        try {
            int finalFontSize = 1;
            if (fontSize == 14) {
                finalFontSize = 1;
            } else if (fontSize == 18) {
                finalFontSize = 2;
            } else if (fontSize == 24) {
                finalFontSize = 3;
            } else if (fontSize == 36) {
                finalFontSize = 4;
            } else if (fontSize == 42) {
                finalFontSize = 5;
            }
            _printingService.setFontZoom(finalFontSize, finalFontSize);
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public Boolean setFontBold(Boolean bold) {
        if (bold == null) {
            bold = false;
        }

        byte[] command = new byte[]{0x1B, 0x45, 0x1};

        if (bold == false) {
            command = new byte[]{0x1B, 0x45, 0x0};
        }

        try {
            _printingService.sendRawData(command);
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }


    public Boolean printColumn(
            String[] stringColumns,
            int[] columnWidth,
            int[] columnAlignment
    ) {


        try {

            _printingService.printColumnsText(
                    stringColumns,
                    columnWidth,
                    columnAlignment
            );

            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public Boolean printImage(Bitmap bitmap) {
        try {
            _printingService.printBitmap(bitmap, 0);
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }


    public Boolean cutPaper() {
        try {
            final int cutResp = _printingService.cutPaper(0, 1);
            return true;
        } catch (RemoteException e) {
            Log.i("Printer", "exception", e);
            return false;
        } catch (NullPointerException e) {
            Log.i("Printer", "exception", e);
            return false;
        }
    }

    public String getPrinterSerialNo() {
        return "";
        // try {
        //     final String serial = _printingService.getPrinterSerialNo();
        //     return serial;
        // } catch (RemoteException e) {
        //     return ""; // error;
        // } catch (NullPointerException e) {
        //     return "NOT FOUND";
        // }
    }

    public String getPrinterVersion() {
        return "";
        // try {
        //     final String version = _printingService.getPrinterVersion();
        //     return version;
        // } catch (RemoteException e) {
        //     return "";// error;
        // } catch (NullPointerException e) {
        //     return "NOT FOUND";
        // }
    }

    public int getPrinterPaper() {
        return 0;
        // try {
        //     final int paper = _printingService.getPrinterPaper();
        //     return paper;
        // } catch (RemoteException e) {
        //     return 1; // error;
        // } catch (NullPointerException e) {
        //     return 1;
        // }
    }

    public int getPrinterMode() {
        return 0;
        // try {
        //     final int mode = _printingService.getPrinterMode();
        //     return mode;
        // } catch (RemoteException e) {
        //     return 3; // error;
        // } catch (NullPointerException e) {
        //     return 3;
        // }
    }

    public Boolean openDrawer() {
        return true;
        // try {
        //     // _printingService.openDrawer(this._callback());
        //     return true;
        // } catch (RemoteException e) {
        //     return false;
        // } catch (NullPointerException e) {
        //     return false;
        // }
    }


    public Boolean drawerStatus() {
        return true;
        // try {
        //     // return  _printingService.getDrawerStatus();
        //     return true;
        // } catch (RemoteException e) {
        //     return false;
        // } catch (NullPointerException e) {
        //     return false;
        // }
    }

    public int timesOpened() {
        return 0;
        // try {
        //     // return  _printingService.getOpenDrawerTimes();
        //     return 0;
        // } catch (RemoteException e) {
        //     return 0;
        // } catch (NullPointerException e) {
        //     return 0;
        // }
    }

    public void lineWrap(int lines) {
        try {
            _printingService.lineWrap(lines);
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    public void sendRaw(byte[] bytes) {
        try {
            _printingService.sendRawData(bytes);
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    public void enterPrinterBuffer(Boolean clear) {
        try {
            // this._printingService.enterPrinterBuffer(clear);
            _printingService.flush();
            _printingService.startTransBuffer();
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    public void commitPrinterBuffer() {
        try {
            // this._printingService.commitPrinterBuffer();
            _printingService.endTransBuffer();
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    public void exitPrinterBuffer(Boolean clear) {
        try {
            // this._printingService.exitPrinterBuffer(clear);
            _printingService.endTransBuffer();
            _printingService.flush();
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    public void setAlignment(int alignment) {
        try {
            _printingService.setAlignMode(alignment);
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    public void printQRCode(String data, int modulesize, int errorlevel) {
        try {
            _printingService.printQrCode(data, modulesize, errorlevel);
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    public void printBarCode(
            String data,
            int barcodeType,
            int textPosition,
            int width,
            int height
    ) {
        try {
            _printingService.printBarCode(
                    data,
                    barcodeType,
                    height,
                    width,
                    textPosition
            );
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }

    // private ICallback _callback() {
    //     return new ICallback() {
    //         @Override
    //         public void onRunResult(boolean isSuccess) throws RemoteException {
    //         }

    //         @Override
    //         public void onReturnString(String result) throws RemoteException {
    //         }

    //         @Override
    //         public void onRaiseException(int code, String msg)
    //                 throws RemoteException {
    //         }

    //         @Override
    //         public void onPrintResult(int code, String msg) throws RemoteException {
    //         }

    //         @Override
    //         public IBinder asBinder() {
    //             return null;
    //         }
    //     };
    // }

    // LCD METHODS

    public void sendLCDCommand(
            int flag
    ) {
        // try {
        //     // _printingService.sendLCDCommand(
        //     //         flag
        //     // );
        // } catch (RemoteException e) {
        // } catch (NullPointerException e) {
        // }
    }

    public void sendLCDString(
            String string
    ) {
        // try {
        //     // _printingService.sendLCDString(
        //     //         string,
        //     //         this._lcdCallback()
        //     // );
        // } catch (RemoteException e) {
        // } catch (NullPointerException e) {
        // }
    }

    public void sendLCDBitmap(
            android.graphics.Bitmap bitmap
    ) {
        // try {
        //     // _printingService.sendLCDBitmap(
        //     //         bitmap,
        //     //         this._lcdCallback()
        //     // );
        // } catch (RemoteException e) {
        // } catch (NullPointerException e) {
        // }
    }

    public void sendLCDDoubleString(
            String topText,
            String bottomText
    ) {
        // try {
        //     // _printingService.sendLCDDoubleString(
        //     //         topText, bottomText,
        //     //         this._lcdCallback()
        //     // );
        // } catch (RemoteException e) {
        // } catch (NullPointerException e) {
        // }
    }

    public void sendLCDFillString(
            String string,
            int size,
            boolean fill
    ) {
        // try {
        //     // _printingService.sendLCDFillString(
        //     //         string, size, fill,
        //     //         this._lcdCallback()
        //     // );
        // } catch (RemoteException e) {
        // } catch (NullPointerException e) {
        // }
    }

    /**
     * Show multi lines text on LCD.
     * @param text Text lines.
     * @param align The weight of the solid content of each line. Like flex.
     */
    public void sendLCDMultiString(
            String[] text,
            int[] align
    ) {
        // try {
        //     // _printingService.sendLCDMultiString(
        //     //         text, align,
        //     //         this._lcdCallback()
        //     // );
        // } catch (RemoteException e) {
        // } catch (NullPointerException e) {
        // }
    }

    // private ILcdCallback _lcdCallback() {
    //     return new ILcdCallback() {
    //         @Override
    //         public IBinder asBinder() {
    //             return null;
    //         }

    //         @Override
    //         public void onRunResult(boolean show) throws RemoteException {
    //         }
    //     };
    // }
}
