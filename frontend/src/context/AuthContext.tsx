import {
  ReactNode,
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState
} from 'react';
import { setAuthToken } from '../api/client';

type AuthContextValue = {
  token: string | null;
  isAuthenticated: boolean;
  login: (token: string) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const AUTH_STORAGE_KEY = 'luckyburger7_token';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (!stored) {
      return null;
    }
    try {
      const parsed = JSON.parse(stored);
      return typeof parsed === 'string' ? parsed : null;
    } catch (error) {
      console.warn('Failed to parse stored token', error);
      return null;
    }
  });

  useEffect(() => {
    setAuthToken(token);
    if (token) {
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(token));
    } else {
      localStorage.removeItem(AUTH_STORAGE_KEY);
    }
  }, [token]);

  const login = useCallback((nextToken: string) => {
    setToken(nextToken);
  }, []);

  const logout = useCallback(() => {
    setToken(null);
  }, []);

  const value = useMemo(
    () => ({
      token,
      isAuthenticated: Boolean(token),
      login,
      logout
    }),
    [login, logout, token]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
