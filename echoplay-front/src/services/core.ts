import type { ApiResponse } from "@/types";

const BASE_URL = import.meta.env.VITE_API_BASE_URL || "/api";

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<ApiResponse<T>> {
    const url = `${this.baseUrl}${endpoint}`;
    const config: RequestInit = {
      credentials: "include",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        ...options.headers,
      },
      ...options,
    };

    const response = await fetch(url, config);
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }

    return response.json();
  }

  async get<T>(endpoint: string, params?: Record<string, string | number | undefined>): Promise<ApiResponse<T>> {
    let url = endpoint;
    if (params) {
      const searchParams = new URLSearchParams();
      for (const [key, value] of Object.entries(params)) {
        if (value !== undefined && value !== null) {
          searchParams.append(key, String(value));
        }
      }
      const qs = searchParams.toString();
      if (qs) url += `?${qs}`;
    }
    return this.request<T>(url, { method: "GET" });
  }

  async post<T>(endpoint: string, body?: Record<string, string | number | undefined>): Promise<ApiResponse<T>> {
    const headers: Record<string, string> = {};
    let requestBody: BodyInit | undefined;

    if (body && Object.values(body).some((v) => v !== undefined)) {
      const formData = new URLSearchParams();
      for (const [key, value] of Object.entries(body)) {
        if (value !== undefined && value !== null) {
          formData.append(key, String(value));
        }
      }
      requestBody = formData.toString();
    }

    return this.request<T>(endpoint, { method: "POST", body: requestBody, headers });
  }
}

export const api = new ApiClient(BASE_URL);
