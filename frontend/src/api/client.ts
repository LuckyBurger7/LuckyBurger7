export const API_BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api';

let authToken: string | null = null;

export function setAuthToken(token: string | null) {
  authToken = token;
}

export interface ApiResponse<T> {
  data: T;
}

export interface ApiPageResponse<T> {
  data: T[];
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
}

export interface ApiErrorResponse {
  error: {
    status: string;
    code: string;
    message: string;
    requestUrl: string;
    timestamp: string;
  };
}

async function handleResponse<T>(response: Response): Promise<T> {
  const text = await response.text();

  if (!response.ok) {
    if (!text) {
      throw new Error(response.statusText);
    }
    try {
      const json = JSON.parse(text) as ApiErrorResponse;
      const errorMessage = json.error?.message ?? response.statusText;
      throw new Error(errorMessage);
    } catch (error) {
      if (error instanceof SyntaxError) {
        throw new Error(response.statusText);
      }
      throw error;
    }
  }

  if (!text) {
    return undefined as T;
  }

  return JSON.parse(text) as T;
}

function buildHeaders(init?: RequestInit): HeadersInit {
  const headers = new Headers(init?.headers ?? {});
  if (!headers.has('Content-Type') && init?.body) {
    headers.set('Content-Type', 'application/json');
  }
  if (authToken && !headers.has('Authorization')) {
    headers.set('Authorization', `Bearer ${authToken}`);
  }
  return headers;
}

export async function apiGet<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    method: 'GET',
    headers: buildHeaders(init)
  });
  return handleResponse<T>(response);
}

export async function apiPost<T>(path: string, body?: unknown, init?: RequestInit): Promise<T> {
  const payload = body ? JSON.stringify(body) : init?.body;
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    method: 'POST',
    headers: buildHeaders({ ...init, body: payload }),
    body: payload
  });
  return handleResponse<T>(response);
}

export async function apiPut<T>(path: string, body?: unknown, init?: RequestInit): Promise<T> {
  const payload = body ? JSON.stringify(body) : init?.body;
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    method: 'PUT',
    headers: buildHeaders({ ...init, body: payload }),
    body: payload
  });
  return handleResponse<T>(response);
}

export async function apiDelete<T>(path: string, body?: unknown, init?: RequestInit): Promise<T> {
  const payload = body ? JSON.stringify(body) : init?.body;
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    method: 'DELETE',
    headers: buildHeaders({ ...init, body: payload }),
    body: payload
  });
  return handleResponse<T>(response);
}

export async function getData<T>(path: string): Promise<T> {
  const response = await apiGet<ApiResponse<T>>(path);
  return response.data;
}

export async function getPagedData<T>(path: string): Promise<ApiPageResponse<T>> {
  return apiGet<ApiPageResponse<T>>(path);
}
