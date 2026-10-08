export default function StreamTable({ columns, data, onRowClick }) {
  return (
    <div style={{ overflowX: 'auto', border: 'var(--border)' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
        <thead style={{ borderBottom: 'var(--border)', backgroundColor: 'var(--color-surface)' }}>
          <tr>
            {columns.map((col, i) => (
              <th key={i} className="label-caps" style={{ padding: 'var(--space-3) var(--space-4)', borderRight: i < columns.length - 1 ? 'var(--border-light)' : 'none' }}>{col.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, i) => (
            <tr
              key={i}
              onClick={() => onRowClick && onRowClick(row)}
              style={{ borderBottom: 'var(--border-light)', cursor: onRowClick ? 'pointer' : 'default', backgroundColor: 'var(--color-paper)' }}
              onMouseOver={(e) => { if (onRowClick) e.currentTarget.style.borderLeft = '3px solid var(--color-accent)' }}
              onMouseOut={(e) => { if (onRowClick) e.currentTarget.style.borderLeft = 'none' }}
            >
              {columns.map((col, j) => (
                <td key={j} style={{ padding: 'var(--space-3) var(--space-4)', borderRight: j < columns.length - 1 ? 'var(--border-light)' : 'none' }}>
                  {col.render ? col.render(row) : row[col.accessor]}
                </td>
              ))}
            </tr>
          ))}
          {data.length === 0 && (
            <tr>
              <td colSpan={columns.length} className="editorial text-muted" style={{ padding: 'var(--space-6)', textAlign: 'center' }}>
                No records found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}
