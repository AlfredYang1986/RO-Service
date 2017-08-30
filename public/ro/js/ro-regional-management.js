/**
 * Created by liwei on 2017/8/21.
 */
layui.use('table', function() {
    var table = layui.table;
    //方法级渲染
    var dataMap = JSON.stringify({});
    AjaxData("/regionalManagement/list", dataMap, "POST", function (data) {
        window.demoTable = table.render({elem: '#rm'
            ,data : data
            //, width: 1100
            , cols: [
                [
                    {space: true, fixed: false, rowspan: '2', title: '#'},
                    {field:'hospital', width:150, align:'center', rowspan:'2', title: '目标医院'},
                    {align:'center', colspan:'3', title: '销售指标'},
                    {align:'center', colspan:'3', title: '费用'},
                    {align:'center', colspan:'3', title: '代表'}
                ],
                [
                    {field:'productA1', width:100, edit: 'text', title: '产品A'}
                    , {field:'productA2', width:100, edit: 'text', title: '产品B'}
                    , {field:'productA3', width:100, edit: 'text', title: '产品C'}
                    , {field:'productB1', width:100, edit: 'text', title: '产品A'}
                    , {field:'productB2', width:100, edit: 'text', title: '产品B'}
                    , {field:'productB3', width:100, edit: 'text', title: '产品C'}
                    , {field:'productC1', width:100, edit: 'text', title: '产品A'}
                    , {field:'productC2', width:100, edit: 'text', title: '产品B'}
                    , {field:'productC3', width:100, edit: 'text', title: '产品C'}
                ]
            ]
            , skin: 'row'
            , even: true
            //, size: 'lg'
            , page: true
            , limits: [10, 20, 50]
            , limit: 10
            //, loading: false
        });
    }, function (err) {
        console.log(err.name);
    })
});