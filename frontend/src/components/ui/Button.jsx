export default function Button({ children, variant = 'primary', ...props }) {
  const baseStyle = {
    padding: 'var(--space-2) var(--space-4)',
    fontFamily: 'var(--font-mono)',
    fontSize: '12px',
    textTransform: 'uppercase',
    letterSpacing: '0.1em',
    border: 'var(--border)',
    transition: 'var(--transition)'
  }

  const variants = {
    primary: { backgroundColor: 'var(--color-ink)', color: 'var(--color-paper)' },
    ghost: { backgroundColor: 'transparent', color: 'var(--color-ink)' },
    danger: { backgroundColor: 'var(--color-accent)', color: 'white', border: '1px solid var(--color-accent)' }
  }

  return (
    <button style={{ ...baseStyle, ...variants[variant], ...props.style }} {...props}>
      {children}
    </button>
  )
}
