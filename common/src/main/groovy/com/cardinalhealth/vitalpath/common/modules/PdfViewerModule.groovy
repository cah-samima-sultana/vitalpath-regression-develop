package com.cardinalhealth.vitalpath.common.modules

class PdfViewerModule extends CommonBaseModule {

    def pdfViewerClassName = "pdf-viewer"
    def textLayerClassName = "textLayer"

    static content = {
        wrapper(wait: true) { $(className(pdfViewerClassName)) }
        textLayer(wait:true) { wrapper.find(className(textLayerClassName))}
    }

    def doesContainValue = {value ->
        waitFor { isDataLoaded() }
        waitFor { textLayer.find("div", text: contains(value)) }
    }

    def isDataLoaded() {
        $("div[data-loaded='true'].page")
    }
}