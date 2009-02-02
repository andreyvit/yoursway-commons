package com.yoursway.commons.excelexport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.yoursway.commons.excelexport.internal.RuntimeIOException;
import com.yoursway.commons.excelexport.internal.SharedStringPool;
import com.yoursway.commons.excelexport.internal.Theme;
import com.yoursway.utils.XmlWriter;
import com.yoursway.utils.YsFileUtils;

public class Workbook {
    
    private SharedStringPool stringPool = new SharedStringPool();
    
    private List<Sheet> sheets = new ArrayList<Sheet>();
    
    public Sheet addSheet(String name) {
        Sheet sheet = new Sheet(sheets.size() + 1, name);
        sheets.add(sheet);
        return sheet;
    }
    
    public void build(OutputStream output) throws IOException {
        try {
            doBuild(output);
        } catch (RuntimeIOException e) {
            throw e.getCause();
        }
    }
    
    private void doBuild(OutputStream output) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(output);
        
        stringPool.add("Foo");
        
        zip.putNextEntry(new ZipEntry("[Content_Types].xml"));
        writeContentTypesXml(zip);
        
        zip.putNextEntry(new ZipEntry("_rels/.rels"));
        writeRelsXml(zip);
        
        zip.putNextEntry(new ZipEntry("docProps/app.xml"));
        writeDocPropsAppXml(zip);
        
        zip.putNextEntry(new ZipEntry("docProps/core.xml"));
        writeDocPropsCoreXml(zip);
        
        zip.putNextEntry(new ZipEntry("xl/_rels/workbook.xml.rels"));
        writeWorkbookRelsXml(zip);
        
        zip.putNextEntry(new ZipEntry("xl/styles.xml"));
        writeStylesXml(zip);
        
        zip.putNextEntry(new ZipEntry("xl/theme/theme1.xml"));
        YsFileUtils.transfer(Theme.getThemeFileAsStream(), zip);
        
        zip.putNextEntry(new ZipEntry("xl/workbook.xml"));
        writeWorkbookXml(zip);
        
        for (Sheet sheet : sheets) {
            zip.putNextEntry(new ZipEntry("xl/worksheets/sheet" + sheet.ordinal() + ".xml"));
            writeSheetXml(zip, sheet);
        }
        
        zip.putNextEntry(new ZipEntry("xl/sharedStrings.xml"));
        writeSharedStringsXml(zip);
        
        zip.finish();
    }
    
    private void writeContentTypesXml(OutputStream out) throws IOException {
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("Types").xmlns("http://schemas.openxmlformats.org/package/2006/content-types");
        xml.tag("Override", "PartName", "/xl/theme/theme1.xml", "ContentType",
            "application/vnd.openxmlformats-officedocument.theme+xml");
        xml.tag("Override", "PartName", "/xl/styles.xml", "ContentType",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml");
        xml.tag("Default", "Extension", "rels", "ContentType",
            "application/vnd.openxmlformats-package.relationships+xml");
        xml.tag("Default", "Extension", "xml", "ContentType", "application/xml");
        xml.tag("Override", "PartName", "/xl/workbook.xml", "ContentType",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml");
        xml.tag("Override", "PartName", "/docProps/app.xml", "ContentType",
            "application/vnd.openxmlformats-officedocument.extended-properties+xml");
        
        for (Sheet sheet : sheets)
            xml.tag("Override", "PartName", "/xl/worksheets/sheet" + sheet.ordinal() + ".xml", "ContentType",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml");
        
        xml.tag("Override", "PartName", "/xl/sharedStrings.xml", "ContentType",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml");
        xml.tag("Override", "PartName", "/docProps/core.xml", "ContentType",
            "application/vnd.openxmlformats-package.core-properties+xml");
        xml.end().finish();
    }
    
    private void writeRelsXml(OutputStream out) throws IOException {
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("Relationships").xmlns("http://schemas.openxmlformats.org/package/2006/relationships");
        xml.tag("Relationship", "Id", "rId3", "Type",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties",
            "Target", "docProps/app.xml");
        xml.tag("Relationship", "Id", "rId2", "Type",
            "http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties",
            "Target", "docProps/core.xml");
        xml.tag("Relationship", "Id", "rId1", "Type",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", "Target",
            "xl/workbook.xml");
        xml.end().finish();
    }
    
    private void writeWorkbookRelsXml(OutputStream out) throws IOException {
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("Relationships").xmlns("http://schemas.openxmlformats.org/package/2006/relationships");
        for (Sheet sheet : sheets)
            xml.tag("Relationship", "Id", "sheetId" + sheet.ordinal(), "Type",
                "http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet", "Target",
                "worksheets/sheet" + sheet.ordinal() + ".xml");
        xml.tag("Relationship", "Id", "rId6", "Type",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings", "Target",
            "sharedStrings.xml");
        xml.tag("Relationship", "Id", "rId5", "Type",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles", "Target",
            "styles.xml");
        xml.tag("Relationship", "Id", "rId4", "Type",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme", "Target",
            "theme/theme1.xml");
        xml.end().finish();
    }
    
    private void writeDocPropsAppXml(OutputStream out) throws IOException {
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("Properties").xmlns(
            "http://schemas.openxmlformats.org/officeDocument/2006/extended-properties").xmlns("vt",
            "http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes");
        xml.tag("Application", "Microsoft Excel");
        xml.tag("DocSecurity", "0");
        xml.tag("ScaleCrop", "false");
        
        xml.start("HeadingPairs").start("vt:vector", "size", "2", "baseType", "variant");
        xml.start("vt:variant").tag("vt:lpstr", "Worksheets").end();
        xml.start("vt:variant").tag("vt:i4", ("" + sheets.size())).end();
        xml.end().end();
        
        xml.start("TitlesOfParts").start("vt:vector", "size", "" + sheets.size(), "baseType", "lpstr");
        for (Sheet sheet : sheets)
            xml.tag("vt:lpstr", sheet.name());
        xml.end().end();
        
        xml.tag("Company");
        xml.tag("LinksUpToDate", "false");
        xml.tag("SharedDoc", "false");
        xml.tag("HyperlinksChanged", "false");
        xml.tag("AppVersion", "12.0000");
        xml.end().finish();
    }
    
    private void writeDocPropsCoreXml(OutputStream out) throws IOException {
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("cp:coreProperties").xmlns("cp",
            "http://schemas.openxmlformats.org/package/2006/metadata/core-properties").xmlns("dc",
            "http://purl.org/dc/elements/1.1/").xmlns("dcterms", "http://purl.org/dc/terms/").xmlns(
            "dcmitype", "http://purl.org/dc/dcmitype/").xmlns("xsi",
            "http://www.w3.org/2001/XMLSchema-instance");
        xml.tag("dc:creator", "Tour Guide Tool");
        xml.tag("cp:lastModifiedBy", "Tour Guide Tool");
        xml.tag("dcterms:created", "xsi:type", "dcterms:W3CDTF", "2009-02-01T22:02:10Z");
        xml.tag("dcterms:modified", "xsi:type", "dcterms:W3CDTF", "2009-02-01T22:37:30Z");
        xml.end().finish();
    }
    
    private void writeSharedStringsXml(OutputStream out) throws IOException {
        List<String> strings = stringPool.entireSequence();
        String size = Integer.toString(strings.size());
        
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("sst").xmlns("http://schemas.openxmlformats.org/spreadsheetml/2006/main").attr("count",
            size).attr("uniqueCount", size);
        for (String string : strings)
            xml.start("si").tag("t", string).end();
        xml.end().finish();
    }
    
    private void writeStylesXml(OutputStream out) throws IOException {
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("styleSheet").xmlns("http://schemas.openxmlformats.org/spreadsheetml/2006/main");
        
        xml.start("fonts", "count", "1");
        xml.start("font").tag("sz", "val", "11").tag("color", "theme", "1").tag("name", "val", "Calibri")
                .tag("family", "val", "2").tag("scheme", "val", "minor").end();
        xml.end();
        
        xml.start("fills", "count", "3");
        xml.start("fill").tag("patternFill", "patternType", "none").end();
        xml.start("fill").tag("patternFill", "patternType", "gray125").end();
        xml.start("fill").start("patternFill", "patternType", "solid").tag("fgColor", "rgb", "FFFF0000").tag(
            "bgColor", "indexed", "64").end().end();
        xml.end();
        
        xml.start("borders", "count", "4");
        xml.start("border").tag("left").tag("right").tag("top").tag("bottom").tag("diagonal").end();
        xml.start("border").start("left", "style", "thin").tag("color", "indexed", "64").end().start("right",
            "style", "thin").tag("color", "indexed", "64").end().start("top", "style", "thin").tag("color",
            "indexed", "64").end().start("bottom", "style", "thin").tag("color", "indexed", "64").end().tag(
            "diagonal").end();
        xml.start("border").start("left", "style", "thin").tag("color", "indexed", "64").end().tag("right")
                .start("top", "style", "thin").tag("color", "indexed", "64").end().start("bottom", "style",
                    "thin").tag("color", "indexed", "64").end().tag("diagonal").end();
        xml.start("border").tag("left").start("right", "style", "thin").tag("color", "indexed", "64").end()
                .start("top", "style", "thin").tag("color", "indexed", "64").end().start("bottom", "style",
                    "thin").tag("color", "indexed", "64").end().tag("diagonal").end();
        xml.end();
        
        xml.start("cellStyleXfs", "count", "1");
        xml.tag("xf", "numFmtId", "0", "fontId", "0", "fillId", "0", "borderId", "0");
        xml.end();
        
        xml.start("cellXfs", "count", "4");
        xml.start("xf", "numFmtId", "0", "fontId", "0", "fillId", "0", "borderId", "0").attr("xfId", "0")
                .end();
        xml.start("xf", "numFmtId", "0", "fontId", "0", "fillId", "0", "borderId", "1").attr("xfId", "0")
                .attr("applyBorder", "1").end();
        xml.start("xf", "numFmtId", "0", "fontId", "0", "fillId", "2", "borderId", "2").attr("xfId", "0")
                .attr("applyFill", "1").attr("applyBorder", "1").attr("applyAlignment", "1").tag("alignment",
                    "horizontal", "center").end();
        xml.start("xf", "numFmtId", "0", "fontId", "0", "fillId", "2", "borderId", "3").attr("xfId", "0")
                .attr("applyFill", "1").attr("applyBorder", "1").attr("applyAlignment", "1").tag("alignment",
                    "horizontal", "center").end();
        xml.end();
        
        xml.start("cellStyles", "count", "1");
        xml.tag("cellStyle", "name", "Normal", "xfId", "0", "builtinId", "0");
        xml.end();
        
        xml.start("dxfs", "count", "0");
        xml.end();
        
        xml.start("tableStyles", "count", "0", "defaultTableStyle", "TableStyleMedium9", "defaultPivotStyle",
            "PivotStyleLight16");
        xml.end();
        
        xml.end().finish();
    }
    
    private void writeWorkbookXml(OutputStream out) throws IOException {
        XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("workbook").xmlns("http://schemas.openxmlformats.org/spreadsheetml/2006/main").xmlns("r",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships");
        xml.tag("fileVersion", "appName", "xl", "lastEdited", "4", "lowestEdited", "4", "rupBuild", "4505");
        xml.tag("workbookPr", "defaultThemeVersion", "124226");
        xml.start("bookViews").tag("workbookView", "xWindow", "480", "yWindow", "30", "windowWidth", "16095",
            "windowHeight", "5580").end();
        xml.start("sheets");
        for (Sheet sheet : sheets)
            xml.tag("sheet", "name", sheet.name(), "sheetId", "" + sheet.ordinal(), "r:id", "sheetId"
                    + sheet.ordinal());
        xml.end();
        
        xml.tag("calcPr", "calcId", "124519");
        xml.end().finish();
    }
    
    private void writeSheetXml(OutputStream out, Sheet sheet) throws IOException {
        final XmlWriter xml = new XmlWriter(out);
        xml.xmlHeader("standalone", "yes");
        xml.start("worksheet").xmlns("http://schemas.openxmlformats.org/spreadsheetml/2006/main").xmlns("r",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships");
        xml.tag("dimension", "ref", "A1:" + sheet.bottomRightCell().name());
        xml.start("sheetViews").start("sheetView", "workbookViewId", "0").end().end();
        // .tag("selection", "activeCell", "B8", "sqref", "B8")
        xml.tag("sheetFormatPr", "defaultRowHeight", "15");
        
        xml.start("sheetData");
        
        Collection<Cell> mergedCells = new ArrayList<Cell>();
        for (Row row : sheet.rows()) {
            List<Cell> cells = row.cells();
            xml.start("row", "r", "" + row.ordinal(), "spans", "1:" + cells.size());
            for (Cell cell : cells) {
                xml.start("c", "r", cell.name());
                cell.acceptContentVisitor(new ContentVisitor() {
                    public void visitNullContent() {
                    }
                    
                    public void visitStringContent(String data) {
                        try {
                            xml.attr("t", "s").tag("v", "" + stringPool.add(data));
                        } catch (IOException e) {
                            throw new RuntimeIOException(e);
                        }
                    }
                    
                });
                xml.end();
                if (cell.horizonalSpan() > 1)
                    mergedCells.add(cell);
            }
            xml.end();
        }
        xml.end();
        
        if (!mergedCells.isEmpty()) {
            xml.start("mergeCells");
            for (Cell cell : mergedCells)
                xml.tag("mergeCell", "ref", cell.name() + ":" + cell.finalMergedCell().name());
            xml.end();
        }
        
        xml.start("pageMargins", "left", "0.7", "right", "0.7", "top", "0.75", "bottom", "0.75").attr(
            "header", "0.3").attr("footer", "0.3").end();
        xml.end().finish();
    }
    
}
