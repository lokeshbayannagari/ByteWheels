/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

kwizolbootstraptable =
{
    commonoptions :
    {
        classes: 'table',
        pagination: true,
        onlyInfoPagination: false,
        striped: false,
        sortable: true,
	maintainSelected:true,
	sidePagination: "client",
        pageSize: 100,
        pageList: [100, 200, 300, 400, 500],
        showColumns: false
    },
    
    getSelectedRowIds : function($table)
    {
        if($table instanceof jQuery)
        {
            var uniqueId = $table.bootstrapTable('getOptions').uniqueId;
            var selectedRows = this.getSelectedRows($table);
            return this.getColumnDataForRows(selectedRows, uniqueId);
        }
        else
        {
            return [];
        }
    },
    
    getSelectedRows : function($table)
    {
        if($table instanceof jQuery)
        {
            return $table.bootstrapTable('getSelections');
        }
        else
        {
            return [];
        }
    },
    
    getTotalRowsCount : function($table)
    {
        if($table instanceof jQuery)
        {
            return $table.bootstrapTable('getOptions').totalRows;
        }
        else
        {
            return 0;
        }
    },
    
    getTotalColumnsCount : function($table)
    {
        return (this.getVisibleColumnsCount($table) + this.getHiddenColumnsCount($table));
    },
    
    getVisibleColumnsCount : function($table)
    {
        if($table instanceof jQuery)
        {
            return $table.bootstrapTable('getVisibleColumns').length;
        }
        else
        {
            return 0;
        }
    },
    
    getHiddenColumnsCount : function($table)
    {
        if($table instanceof jQuery)
        {
            return $table.bootstrapTable('getHiddenColumns').length;
        }
        else
        {
            return 0;
        }
    },
    
    getColumnData : function($table, columnField)
    {
        if($table instanceof jQuery)
        {
            return this.getColumnDataForRows(this.getAllRows($table), columnField);
        }
        else
        {
            return [];
        }
    },
    
    getAllRows : function($table)
    {
        if($table instanceof jQuery)
        {
            return $table.bootstrapTable('getData');
        }
        else
        {
            return [];
        }
    },
    
    getAllRowsIds : function($table)
    {
        if($table instanceof jQuery)
        {
            var uniqueId = $table.bootstrapTable('getOptions').uniqueId;
            return this.getColumnDataForRows(this.getAllRows($table), uniqueId);
        }
        else
        {
            return [];
        }
    },
    
    getColumnDataForRows : function(rows, columnField)
    {
        var columnData = [];
        if(rows && rows !== null && columnField && columnField !== null && Array.isArray(rows))
        {
            for(var index = 0; index < rows.length; index++)
            {
                columnData.push(rows[index][columnField]);
            }
        }
        return columnData;
    },
    
    getRowIndexByUniqueId : function($table, idValue)
    {
        var nRowIndex = -1;
        if($table instanceof jQuery && idValue)
        {
            var uniqueId = $table.bootstrapTable('getOptions').uniqueId;
            var arrUniqueIds = this.getColumnData($table, uniqueId)
            
            nRowIndex = arrUniqueIds.indexOf(idValue);
        }
        return nRowIndex;
    },
    
    getHighlightedRowId : function($table)
    {
        var uniqueId = null;
        var $tr = this.getHighlightedRowEle($table);
        if($tr instanceof jQuery)
        {
            uniqueId = $tr.attr('data-uniqueid');
        }
        return uniqueId;
    },
    
    getHighlightedRowIndex : function($table)
    {
        var nRowIndex = -1;
        var $tr = this.getHighlightedRowEle($table);
        if($tr instanceof jQuery)
        {
            nRowIndex = $tr.attr('data-index');
        }
        return nRowIndex;
    },
    
    getHighlightedRowEle : function($table)
    {
        var $tr = null;
        if(this.getTotalRowsCount($table) > 0)
        {
            var $tr = $table.find('tr.highlightRow');
        }
        return $tr;
    },
    
    getHighlightedRowData : function($table)
    {
        var rowData = null;
        var uniqueId = this.getHighlightedRowId($table);
        if(uniqueId != null)
        {
            return $table.bootstrapTable('getRowByUniqueId', uniqueId);
        }
        return rowData;
    },
    
    highlightRow : function($element)
    {
        if($element instanceof jQuery)
        {
            $element.parent().find('tr.highlightRow').removeClass('highlightRow');
            $element.addClass('highlightRow');
        }
    },
    
    rowStyle : function(row, index)
    {
        return {classes: index == 0 ? "highlightRow" : ""};
    },
    
    highlightRowByID : function(rowID, $table)
    {
        if(rowID && rowID != null && $table instanceof jQuery)
        {
            var $ele = $table.find("[data-uniqueid='" + rowID + "']");
            this.highlightRow($ele);
        }
    }
    
    
};


