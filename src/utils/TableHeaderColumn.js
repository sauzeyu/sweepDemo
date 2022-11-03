export function TableHeaderColumn(text, record, index, dataTable) {
  let name = dataTable?.props?.name;
  let currentIndex = dataTable?.props?.easyTableProvider?.page[name].current;
  let currentPageSize =
    dataTable?.props?.easyTableProvider?.page[name].pageSize;
  return (currentIndex - 1) * currentPageSize + (index + 1);
}
